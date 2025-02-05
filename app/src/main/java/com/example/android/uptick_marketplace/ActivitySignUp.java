package com.example.android.uptick_marketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivitySignUp extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private EditText EmailText, PasswordText, fName, lName, mobNum;
    Spinner UnivSpinner, DormSpinner;
    String univText,dormText;
    private Button SignUpButton;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Setting up University Name Spinner
        UnivSpinner = (Spinner) findViewById(R.id.university_spinner);
        UnivSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> UnivAdapter = ArrayAdapter.createFromResource(this,
                R.array.university_name_array,android.R.layout.simple_spinner_item);
        UnivAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UnivSpinner.setAdapter(UnivAdapter);

        // Setting up Dorm Spinner
        DormSpinner = (Spinner) findViewById(R.id.dorm_spinner);
        DormSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> DormAdapter = ArrayAdapter.createFromResource(this,
                R.array.dorm_array,android.R.layout.simple_spinner_item);
        DormAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DormSpinner.setAdapter(DormAdapter);

        SignUpButton = findViewById(R.id.sign_up_continue_button);
        SignUpButton.setOnClickListener(this);

        EmailText = (EditText)findViewById(R.id.type_signup_email);
        PasswordText = (EditText) findViewById(R.id.type_sign_up_confirm_password);
        fName = findViewById(R.id.type_fname);
        lName = findViewById(R.id.type_lname);
        mobNum = findViewById(R.id.type_mobilenum);

        progressBar = findViewById(R.id.sign_up_progressbar);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        univText = UnivSpinner.getSelectedItem().toString();
        dormText = DormSpinner.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addUserProfile(){

        //user = mAuth.getInstance().getCurrentUser();
        Map<String, Object> profile = new HashMap<>();
        if (user!= null){profile.put("UserID", user.getUid());}
        profile.put("First Name",fName.getText().toString().trim());
        profile.put("Last Name", lName.getText().toString().trim());
        profile.put("email", EmailText.getText().toString().trim());
        profile.put("Mobile Number", mobNum.getText().toString().trim());
        profile.put("University Name", univText);
        profile.put("Dorm Name", dormText);

        String doc_id = db.collection("users").document(user.getUid()).getId();
        db.collection("users")
                .document(doc_id)
                .set(profile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Profile Info added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to add Profile", Toast.LENGTH_SHORT).show();
                    }
                });

        startActivity(new Intent(ActivitySignUp.this, Main_Activity.class));


    }

    private void registerUser(){
        String email =EmailText.getText().toString().trim();
        String password = PasswordText.getText().toString().trim();


        if (email.isEmpty()){
            EmailText.setError("Email is required");
            EmailText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            EmailText.setError("Please enter a valid email");
            EmailText.requestFocus();
            return;
        }


        if(password.isEmpty()){
            PasswordText.setError("Password is required");
            PasswordText.requestFocus();
            return;
        }

        if(password.length()<6){
            PasswordText.setError("Minimum length of password should be 6");
            PasswordText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        SignUpButton.setEnabled(false);



        // After all checks are passed new account is being made
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    user = mAuth.getCurrentUser();
                    addUserProfile();

//              TODO: Implement Email verification; current problem is figuring out if AuthStateListener is working
//                    mAuthListener = new FirebaseAuth.AuthStateListener() {
//                        @Override
//                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                            Toast.makeText(getApplicationContext(),"Verifying Email", Toast.LENGTH_SHORT).show();
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
//                            if (user != null) {
//                                // User is signed in
//                                // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
//                                // the user will receive another verification email.
//
//                                sendVerificationEmail();
//                            }
//
//                        }
//                    };
                    progressBar.setVisibility(View.GONE);
                    finish();

                    Toast.makeText(getApplicationContext(),"Sign Up is successful", Toast.LENGTH_SHORT).show();



                }else{
                    SignUpButton.setEnabled(true);
                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"You are already registered", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }

    private void sendVerificationEmail() {
        /*
        ------------------------------------------------------------------------------
         In this method, Firebase auto generates email verification link to be sent to
         user's email
        ------------------------------------------------------------------------------
        */
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(ActivitySignUp.this, ActivityLogin.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity
                            Toast.makeText(getApplicationContext(),"Sending Email was unsuccessful, please use a valid email",
                                    Toast.LENGTH_SHORT).show();
                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_continue_button:
                registerUser();
                break;

        }

    }
}

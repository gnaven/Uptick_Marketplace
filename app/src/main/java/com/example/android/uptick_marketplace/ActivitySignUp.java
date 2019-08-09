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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class ActivitySignUp extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private EditText EmailText;
    private EditText PasswordText;
    private Button SignUpButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        // Setting up University Name Spinner
        Spinner UnivSpinner = (Spinner) findViewById(R.id.university_spinner);
        UnivSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> UnivAdapter = ArrayAdapter.createFromResource(this,
                R.array.university_name_array,android.R.layout.simple_spinner_item);
        UnivAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        UnivSpinner.setAdapter(UnivAdapter);

        // Setting up Dorm Spinner
        Spinner DormSpinner = (Spinner) findViewById(R.id.dorm_spinner);
        DormSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> DormAdapter = ArrayAdapter.createFromResource(this,
                R.array.dorm_array,android.R.layout.simple_spinner_item);
        DormAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DormSpinner.setAdapter(DormAdapter);

        SignUpButton = findViewById(R.id.sign_up_continue_button);
        SignUpButton.setOnClickListener(this);

        EmailText = (EditText)findViewById(R.id.type_signup_email);
        PasswordText = (EditText) findViewById(R.id.type_sign_up_confirm_password);

        progressBar = findViewById(R.id.sign_up_progressbar);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                    finish();
                    FirebaseUser user = mAuth.getCurrentUser();
                    

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Sign Up is successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ActivitySignUp.this, Main_Activity.class));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_continue_button:
                registerUser();
                break;

        }

    }
}

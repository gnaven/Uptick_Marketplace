package com.example.android.uptick_marketplace;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    private EditText EmailText;
    private EditText PasswordText;
    private Button SignInButton;
    private Button SignUpButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EmailText = findViewById(R.id.type_email);
        PasswordText = findViewById(R.id.type_password);

        SignInButton = findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(this);
        SignUpButton = findViewById(R.id.sign_up_button);
        SignUpButton.setOnClickListener(this);
        progressBar = findViewById(R.id.login_progressbar);

        findViewById(R.id.forgot_pass_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    private void userlogin(){

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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    finish();
                    Intent Login = new Intent(ActivityLogin.this, Main_Activity.class);
                    Login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(Login);

                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, Main_Activity.class));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.forgot_pass_button:

                break;
            case R.id.sign_up_button:
                // directs to ActivitySignUp
                finish();
                startActivity(new Intent(this, ActivitySignUp.class));
                break;
            case R.id.sign_in_button:
                // starting logging in methods
                userlogin();

        }
    }

}

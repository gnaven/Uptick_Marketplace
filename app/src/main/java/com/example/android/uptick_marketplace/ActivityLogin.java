package com.example.android.uptick_marketplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    private EditText EmailText;
    private EditText PasswordText;
    private Button SignInButton;
    private Button SignUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailText = findViewById(R.id.type_email);
        PasswordText = findViewById(R.id.type_password);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_up_button).setOnClickListener(this);


        findViewById(R.id.forgot_pass_button).setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.forgot_pass_button:

                break;
        }
    }
}

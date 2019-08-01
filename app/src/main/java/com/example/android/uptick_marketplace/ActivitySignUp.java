package com.example.android.uptick_marketplace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ActivitySignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Spinner UnivSpinner = (Spinner) findViewById(R.id.university_spinner);

        // Need to finish making spinner
        // https://developer.android.com/guide/topics/ui/controls/spinner
//        ArrayAdapter<CharSequence> UnivAdapter = ArrayAdapter.createFromResource(this,
//                R.array.university_name_array,R.layout.)
    }
}

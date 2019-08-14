package com.example.android.uptick_marketplace;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProductListing extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private TextView sell_text;
    private Toolbar Sell_toolbar;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private EditText productName, productDescription, productPrice;
    private Button addPictureButton, greatConditionButton, goodConditionButton, workingConditionButton;
    private String productCondition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setContentView(R.layout.activity_sell);
        //Sell_toolbar = (Toolbar) findViewById(R.id.sell_toolbar);

        //Setting Spinner for Category Selection
        Spinner CategorySpinner = (Spinner) findViewById(R.id.product_category_spinner);
        CategorySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> CategoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.product_category_array,android.R.layout.simple_spinner_item);
        CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CategorySpinner.setAdapter(CategoryAdapter);

        addPictureButton = findViewById(R.id.addproduct_picture_button);
        addPictureButton.setOnClickListener(this);

        greatConditionButton = findViewById(R.id.great_cond_button);
        greatConditionButton.setOnClickListener(this);

        goodConditionButton = findViewById(R.id.good_cond_button);
        goodConditionButton.setOnClickListener(this);

        workingConditionButton = findViewById(R.id.working_cond_button);
        workingConditionButton.setOnClickListener(this);

        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description_text);
        productPrice = findViewById(R.id.product_price);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.addproduct_picture_button:
                // TODO: implement Image adding functionality
                break;
            case R.id.great_cond_button:
                greatConditionButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                goodConditionButton.setBackgroundColor(getResources().getColor(R.color.white));
                workingConditionButton.setBackgroundColor(getResources().getColor(R.color.white));
                productCondition = "Great";
                break;
            case R.id.good_cond_button:
                goodConditionButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                greatConditionButton.setBackgroundColor(getResources().getColor(R.color.white));
                workingConditionButton.setBackgroundColor(getResources().getColor(R.color.white));
                productCondition = "Good";
                break;
            case R.id.working_cond_button:
                workingConditionButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                greatConditionButton.setBackgroundColor(getResources().getColor(R.color.white));
                goodConditionButton.setBackgroundColor(getResources().getColor(R.color.white));
                productCondition = "Working";
                break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

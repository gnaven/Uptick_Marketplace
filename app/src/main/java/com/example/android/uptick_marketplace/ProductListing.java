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

public class ProductListing extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private TextView sell_text;
    private Toolbar Sell_toolbar;

    private EditText productName, productDescription, productPrice;
    private Button addPictureButton, greatConditionButton, goodConditionButton, poorConditionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sell);
        //Sell_toolbar = (Toolbar) findViewById(R.id.sell_toolbar);

        //Setting Spinner for Category Selection
        Spinner CategorySpinner = (Spinner) findViewById(R.id.product_category_spinner);
        CategorySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> CategoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.product_category_array,android.R.layout.simple_spinner_item);
        CategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CategorySpinner.setAdapter(CategoryAdapter);





    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

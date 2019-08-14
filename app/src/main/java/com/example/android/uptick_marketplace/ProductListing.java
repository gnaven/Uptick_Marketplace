package com.example.android.uptick_marketplace;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductListing extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private TextView sell_text;
    private Toolbar Sell_toolbar;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private DatabaseReference mDatabase;

    private EditText productName, productDescription, productPrice;
    private Button addPictureButton, greatConditionButton, goodConditionButton, workingConditionButton, confirmListingButton;
    private String productCondition;
    private String productCategory;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_sell);
        //Sell_toolbar = (Toolbar) findViewById(R.id.sell_toolbar);

        //Setting Spinner for Category Selection
        Spinner CategorySpinner = (Spinner) findViewById(R.id.product_category_spinner);
        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productCategory = (String) adapterView.getItemAtPosition(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
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

        confirmListingButton = findViewById(R.id.listing_confirmation_button);
        confirmListingButton.setOnClickListener(this);

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
            case R.id.listing_confirmation_button:
                // submit product to be added to the database
                String prodName = productName.getText().toString().trim();
                String prodDesc = productDescription.getText().toString().trim();
                int prodPrice = Integer.parseInt(productPrice.getText().toString().trim());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();

                Map<String, Object> product = new HashMap<>();
                product.put("name", prodName);
                product.put("description", prodDesc);
                product.put("Category",productCategory);
                product.put("Condition", productCondition);
                product.put("Timestamp", date);
                product.put("price", prodPrice);
                product.put("UserID", user.getUid());
                db.collection("products")
                        .add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getApplicationContext(),"submitted product for listing", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });


//
//                Product product = new Product(prodName,prodDesc, productCategory ,productCondition,
//                        prodPrice,date);
//
//                mDatabase.child("products").child(user.getUid()).setValue(product);

                //Toast.makeText(getApplicationContext(),"submitted product for listing", Toast.LENGTH_SHORT).show();

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

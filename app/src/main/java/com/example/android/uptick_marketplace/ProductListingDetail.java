package com.example.android.uptick_marketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;

public class ProductListingDetail extends AppCompatActivity implements View.OnClickListener{

    private Button markSold, markHold, deleteItem;
    private TextView productName, condition, price, description, listingTime, seller_name;

    private boolean productOwner;

    //Firebase config
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        HashMap<String,Object> productInfo = (HashMap) getIntent().getSerializableExtra("Product_Details");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user.toString() == productInfo.get("UserID").toString()){
            productOwner = true;
            String ownerName = "You";
        }else{
            productOwner = false;

        }

        markSold = findViewById(R.id.product_detail_mark_sold);
        markSold.setOnClickListener(this);

        markHold = findViewById(R.id.product_detail_put_hold);
        markHold.setOnClickListener(this);

        deleteItem = findViewById(R.id.product_details_delete_listing);
        deleteItem.setOnClickListener(this);


        //Setting the texts
        productName = findViewById(R.id.product_details_name);
        productName.setText(productInfo.get("name").toString());

        condition = findViewById(R.id.product_details_condition);
        condition.setText(productInfo.get("condition").toString()+ " condition");

        price = findViewById(R.id.product_details_price);
        price.setText("$ "+productInfo.get("price").toString());

        description = findViewById(R.id.product_details_description_text);
        description.setText(productInfo.get("description").toString());

        listingTime = findViewById(R.id.product_details_listing_time);
        // TODO: make new method that handles time and what time format to show
        Date dateNow = new Date();
        Date listedDate = (Date)productInfo.get("timestamp");
        long diff = dateNow.getTime() - listedDate.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        listingTime.setText("Listed "+ diffSeconds+" seconds ago");

        seller_name = findViewById(R.id.product_detail_seller_name);
        seller_name.setText("Sold by "+productInfo.get("UserID").toString());





        /*
        TODO:
        1) Check if User is the same as product lister
        2) Collect Product name, price, condition, description, posted time, seller name
         */


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.product_detail_mark_sold:
                // set the availability of the product to false
                break;
            case R.id.product_details_delete_listing:

                break;
            case R.id.product_detail_put_hold:

                break;
        }

    }
}

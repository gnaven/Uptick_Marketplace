package com.example.android.uptick_marketplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;

public class ProductListingDetail extends AppCompatActivity implements View.OnClickListener{
    /*
    This class is used for the logic behind showing products in detail
    in the product_details.xml file.
    According to whether the user is the seller or not, this class controls how the information
    is showed in the product_details.xml layout.
    The main differences are as follows:
    1) Seller name
    2) Seller privileges to edit the the product, control the product status layout
    3) For the general user contact seller bar is available with no selling abilities
     */

    private Button markSold, markHold, deleteItem;
    private TextView productName, condition, price, description, listingTime, seller_name;

    private boolean productOwner;

    private ImageView product_image;

    //Firebase config
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;

    String firstname = "A";
    String lastname = "B";

    String doc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        HashMap<String,Object> productInfo = (HashMap) getIntent().getSerializableExtra("Product_Details");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        String ownerName ="";
        //TODO: Figure out why retrieveSeller is slow on calls
        if (user.getUid().equals(productInfo.get("UserID").toString())){
            productOwner = true;
            ownerName = "You";
        }else{
            productOwner = false;
            retrieveSeller(user.getUid());
            ownerName = firstname+" "+lastname;
        }
        markSold = findViewById(R.id.product_detail_mark_sold);
        markSold.setOnClickListener(this);

        markHold = findViewById(R.id.product_detail_put_hold);
        markHold.setOnClickListener(this);
        if (!(boolean)productInfo.get("available")){
            markHold.setText("Remove From Hold");
        }

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

        Date listedDate = (Date)productInfo.get("timestamp");
        String timedDiff = retrieveTime(listedDate);

        listingTime.setText("Listed "+ timedDiff);

        seller_name = findViewById(R.id.product_detail_seller_name);
        seller_name.setText("Sold by "+ownerName);

        product_image = findViewById(R.id.product_detail_image);
        Uri fileUri = Uri.parse(productInfo.get("image_filepath").toString());
        product_image.setImageURI(fileUri);

        doc_id = productInfo.get("document_id").toString();

        /*
        TODO:
        1) Check if User is the same as product lister
        2) Collect Product name, price, condition, description, posted time, seller name
         */
    }

    Object retrievedItem = "";
    public Object getItem(final String field){
        DocumentReference docRef = db.collection("users").document("products");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        retrievedItem = document.get(field);
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
        return retrievedItem;
    }

    public void deleteItem(){
        /*
        This method deletes the document and starts a new homepage activity
         */
        db.collection("products").document(doc_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Deleted Item", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProductListingDetail.this,Main_Activity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Item not deleted", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public String retrieveTime(Date listedDate){
        Date dateNow = new Date();
        long diff = dateNow.getTime() - listedDate.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        if (diffSeconds < 60){
            return diffSeconds + " seconds ago";
        }else if (diffMinutes <=60 ){
            return diffMinutes + " minutes ago";
        }else if (diffHours <= 24){
            return diffHours + " hours ago";
        }else{
            return diffDays + " days ago";
        }
    }


    public void retrieveSeller(String userID){
        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        firstname = document.getString("First Name");
                        lastname = document.getString("Last Name");
                    } else {
                        Toast.makeText(getApplicationContext(),"Document not found", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Task not succesful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.product_detail_mark_sold:
                // set the availability of the product to false
                db.collection("products")
                        .document(doc_id)
                        .update("available", false);
                Toast.makeText(getApplicationContext(),"Product marked as sold", Toast.LENGTH_SHORT).show();
                break;
            case R.id.product_details_delete_listing:
                deleteItem();
                break;
            case R.id.product_detail_put_hold:
                if (markHold.getText().equals("Put On Hold")){
                    markHold.setText("Remove From Hold");
                    db.collection("products")
                            .document(doc_id)
                            .update("available", false);
                }else{
                    db.collection("products")
                            .document(doc_id)
                            .update("available", true);
                    markHold.setText("Put On Hold");
                }

                break;
        }

    }
}

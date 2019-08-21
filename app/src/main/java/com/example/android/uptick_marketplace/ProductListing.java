package com.example.android.uptick_marketplace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProductListing extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private TextView sell_text;
    private Toolbar Sell_toolbar;

    //Firebase variables
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseFirestore db;


    private EditText productName, productDescription, productPrice;
    private Button addPictureButton, greatConditionButton, goodConditionButton, workingConditionButton, confirmListingButton;
    private String productCondition;
    private String productCategory;
    private ImageView listing_image;
    private Uri listing_filepath;
    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_sell);

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

        listing_image = findViewById(R.id.list_product_image);

        addPictureButton = findViewById(R.id.listing_add_picture_button);
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            listing_filepath = data.getData();
            Toast.makeText(ProductListing.this, listing_filepath.toString(), Toast.LENGTH_SHORT).show();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), listing_filepath);
                listing_image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(listing_filepath != null)
        {
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();

            // TODO: Need to fix uploading pictures to db. Look into binary image loading
            storageReference = storage.getInstance().getReference();
            //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
            StorageReference ref = storageReference.child("product_photos/"+ user.getUid().toString()+ "/"+listing_filepath.getLastPathSegment()+".jpg");
            Toast.makeText(ProductListing.this, "Upload ing image", Toast.LENGTH_SHORT).show();
            ref.putFile(listing_filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //progressDialog.dismiss();
                            Toast.makeText(ProductListing.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressDialog.dismiss();
                            Toast.makeText(ProductListing.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

//            case R.id.addproduct_picture_button:
//                // TODO: implement Image adding functionality
//                break;

            case R.id.listing_add_picture_button:
                chooseImage();
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
                product.put("category",productCategory);
                product.put("condition", productCondition);
                product.put("timestamp", date);
                product.put("price", prodPrice);
                product.put("UserID", user.getUid());
                product.put("available", true);
                if (listing_filepath !=null){product.put("image_filepath",listing_filepath.toString());}

                uploadImage();
                String doc_id = db.collection("products").document().getId();
                db.collection("products")
                        .document(doc_id)
                        .set(product)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"submitted product for listing", Toast.LENGTH_SHORT).show();
                        }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Failed to list product", Toast.LENGTH_SHORT).show();
                            }
                        });


                product.put("document_id", doc_id);
                Intent product_details = new Intent(ProductListing.this, ProductListingDetail.class);
                product_details.putExtra("Product_Details",(Serializable) product);
                startActivity(product_details);
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
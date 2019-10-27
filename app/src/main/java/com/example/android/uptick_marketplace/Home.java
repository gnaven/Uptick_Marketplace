package com.example.android.uptick_marketplace;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


//TODO: find out how to cache images so that it is not required to be loaded everytime
public class Home extends Fragment {

    private GridView gvGallery;
    private GridBaseAdapter gridBaseAdapter;
    private ArrayList<ImageModel> imageModelArrayList;
    private ProgressBar progressBar;

    //Firebase config
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseFirestore db;

    ArrayList<String> doclist = new ArrayList<>();
    HashMap<String,Object> productNameList= new HashMap<>();

    private View globalView;


    private int[] myImageList = new int[]{
            R.drawable.fridge,R.drawable.fridge,
            R.drawable.printer,R.drawable.printer};

    private String[] myImageNameList = new String[]{"Fridge", "Bag"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.activity_home,null);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        progressBar = view.findViewById(R.id.home_progressbar);
        // Initializing getting all docids;
        getAllDocID();
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                2);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //This runs after 3 seconds
                if (counter>=doclist.size()){
                    gridBaseAdapter = new GridBaseAdapter(globalView.getContext(),productNameList
                            ,spaceRef,imageBytesList,productList);
                    gvGallery.setAdapter(gridBaseAdapter);
                    handler.removeCallbacks(this);
                    progressBar.setVisibility(view.INVISIBLE);

                }else{
                    handler.postDelayed(this, 2000);
                }

            }
        }, 4000);  //the time is in miliseconds
        globalView = view;

        //Calling GridBasAdapter to populate images in the homepage
        gvGallery = view.findViewById(R.id.gv);
        imageModelArrayList = populateList();

        gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // position number here should be the same as data stored in product list
                final HashMap<String, Object> product = productList.get(position);
                final String docid = product.get("doc_id").toString();

                DocumentReference docRef = db.collection("products").document(docid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                product.put("document_id", docid);
                                product.put("description", document.getString("description"));
                                product.put("category",document.getString("category"));
                                product.put("condition", document.getString("condition"));
                                product.put("timestamp", document.get("timestamp"));
                                product.put("price", document.get("price"));
                                product.put("UserID", document.getString("UserID"));

                                product.put("available", document.get("available"));


                                byte[] productImageBytes = (byte[]) product.get("imageBytes");

                                Bitmap bitmap = BitmapFactory.decodeByteArray(productImageBytes, 0, productImageBytes.length);
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), bitmap, "Uptick_Image", null);
                                Uri imageUri = Uri.parse(path);

                                Toast.makeText(getActivity(),imageUri+"clicked", Toast.LENGTH_SHORT).show();
                                Intent product_details = new Intent(getActivity(), ProductListingDetail.class);
                                product.put("image_filepath", imageUri);
                                product.remove("imageBytes");
                                product_details.putExtra("Product_Details",(Serializable) product);
                                startActivity(product_details);
                            }
                        }
                    }
                });

            }
        });

        view.findViewById(R.id.home_text);
        // Send to sell page
        view.findViewById(R.id.list_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sell_activity = new Intent(getActivity(), ProductListing.class);
                startActivity(sell_activity);

            }
        });
    }


    private ArrayList<String> getAllDocID (){
        /*
        method retrieves all doc id from the products collection
        calls storageRefImageLoader method to retrieve all the storage references.
         */
        doclist.clear();
        db.collection("products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        doclist.add(document.getId());
                    }
                    // Populating list for product name
                    for (final String docid: doclist){
                        DocumentReference docRef = db.collection("products").document(docid);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        productNameList.put(docid,document.getString("name"));
                                    }
                                }
                            }
                        });

                    }
                    // Calls on loading the storage reference
                    for (String docid: doclist){
                        storageRefImageLoader(docid);
                    }

                    Log.d("LOGGER", doclist.toString());
                } else {
                    Toast.makeText(getActivity(),"failed in doc", Toast.LENGTH_SHORT).show();
                    Log.d("LOGGER", "Error getting documents: ", task.getException());
                }
            }
        });

        return doclist;
    }


    // Counter keeps track of how many storage pictures have been converted into bytes
    private int counter=0;
    Object image_reference= "";
    ArrayList<StorageReference> spaceRef = new ArrayList<>();
    HashMap<Integer, HashMap<String,Object>> productList = new HashMap<>();
    ArrayList<byte[]> imageBytesList = new ArrayList<byte[]>();
    public void storageRefImageLoader(final String doc_id){
        DocumentReference docRef = db.collection("products").document(doc_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                //Toast.makeText(getActivity(),"got complete images", Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(getActivity(),"got images", Toast.LENGTH_SHORT).show();
                        image_reference = document.getString("storage_path");
                        spaceRef.add(storageReference.child(image_reference.toString()));
                        final long ONE_MEGABYTE = 1024 * 1024;
                        storageReference.child(image_reference.toString())
                                .getBytes(20*ONE_MEGABYTE)
                                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        HashMap<String, Object>productInfo = new HashMap<String,Object>();
                                        // TODO add all fields
                                        productInfo.put("doc_id",doc_id);
                                        productInfo.put("name", productNameList.get(doc_id));
                                        productInfo.put("imageBytes", bytes);

                                        productList.put(counter,productInfo);

                                        counter++;
                                    }
                                });


                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

    }

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 2; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setName(myImageNameList[i]);
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

}

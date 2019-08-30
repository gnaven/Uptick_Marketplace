package com.example.android.uptick_marketplace;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Home extends Fragment {

    private GridView gvGallery;
    private GridBaseAdapter gridBaseAdapter;
    private ArrayList<ImageModel> imageModelArrayList;
    private ImageView testGlide;
    private TextView testText;

    //Firebase config
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseFirestore db;

    private View view;


    private int[] myImageList = new int[]{
            R.drawable.fridge,R.drawable.fridge,
            R.drawable.printer};

    private String[] myImageNameList = new String[]{"Fridge", "Bag",
            "Printer"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.activity_home,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //TODO: remove these after test
        testGlide = view.findViewById(R.id.test_product_image);
        testText = view.findViewById(R.id.test_text);

        ArrayList<String> allDocID = getAllDocID();
        ArrayList<StorageReference> storageReferenceList = new ArrayList<>();
//        for (String docid: allDocID){
//            StorageReference cloudImageReference = storageRefImageLoader(docid);
//            storageReferenceList.add(cloudImageReference);
//        }



        //Toast.makeText(getActivity(), storageReferenceList.get(0).toString(), Toast.LENGTH_SHORT).show();
//        Glide.with(this)
//                .load(storageReferenceList.get(0))
//                .into(testGlide);


        //Calling GridBasAdapter to populate images in the homepage
        gvGallery = view.findViewById(R.id.gv);
        imageModelArrayList = populateList();

        gridBaseAdapter = new GridBaseAdapter(view.getContext(),imageModelArrayList,storageReferenceList);
        gvGallery.setAdapter(gridBaseAdapter);

        gvGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText((getActivity()), myImageNameList[position]+" Clicked", Toast.LENGTH_SHORT).show();
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

    ArrayList<String> doclist = new ArrayList<>();
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

                    testText.setText(Integer.toString(doclist.size()));
                    ArrayList<StorageReference> storageReferenceList = new ArrayList<>();
                    for (String docid: doclist){
                        testText.setText(Integer.toString(doclist.size()));
                        //StorageReference cloudImageReference = storageRefImageLoader(docid);
                        //storageReferenceList.add(cloudImageReference);
                        storageRefImageLoader(docid);
                    }
                    testText.setText("Space Ref "+Integer.toString(spaceRef.size()));

                    Log.d("LOGGER", doclist.toString());
                } else {
                    Toast.makeText(getActivity(),"failed in doc", Toast.LENGTH_SHORT).show();
                    Log.d("LOGGER", "Error getting documents: ", task.getException());
                }
            }
        });

        return doclist;
    }



    Object image_reference= "";
    ArrayList<StorageReference> spaceRef = new ArrayList<>();
    public void storageRefImageLoader(String doc_id){
//    public StorageReference storageRefImageLoader(String doc_id){
        DocumentReference docRef = db.collection("products").document(doc_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Toast.makeText(getActivity(),"got complete images", Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(getActivity(),"got images", Toast.LENGTH_SHORT).show();
                        image_reference = document.getString("storage_path");
                        Toast.makeText(getActivity(),image_reference.toString(), Toast.LENGTH_SHORT).show();
                        spaceRef.add(storageReference.child(image_reference.toString()));

                        //TODO: This works
                        final long ONE_MEGABYTE = 1024 * 1024;
                        //download file as a byte array
                        storageReference.child(image_reference.toString())
                                .getBytes(2*ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                testGlide.setImageBitmap(bitmap);
                            }
                        });
//                        Glide.with(Home.this)
//                                .load(storageReference.child(image_reference.toString()))
//                                .into(testGlide);


                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
        Toast.makeText(getActivity(),spaceRef.toString(), Toast.LENGTH_SHORT).show();

//        Toast.makeText(getActivity(),spaceRef[0].toString(), Toast.LENGTH_SHORT).show();
//        return spaceRef[0];
    }

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setName(myImageNameList[i]);
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

}

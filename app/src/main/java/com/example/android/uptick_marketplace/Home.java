package com.example.android.uptick_marketplace;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends Fragment {

    private GridView gvGallery;
    private GridBaseAdapter gridBaseAdapter;
    private ArrayList<ImageModel> imageModelArrayList;

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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Calling GridBasAdapter to populate images in the homepage
        gvGallery = view.findViewById(R.id.gv);

        imageModelArrayList = populateList();

        gridBaseAdapter = new GridBaseAdapter(view.getContext(),imageModelArrayList);
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
                //Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
                //toolbartext.setText("ProductListing");
                //Toast.makeText(getActivity(), "Clicked Profile Button", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

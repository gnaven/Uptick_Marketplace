package com.example.android.uptick_marketplace;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class GridBaseAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context ctx;
    private HashMap<String,Object> imageNameArrayList;
    private ImageView ivGallery;
    private TextView textView;
    ArrayList<byte[]> imageBytesList;

    private ArrayList<StorageReference> storageReferenceList;
    FirebaseStorage storage;
    HashMap<Integer, HashMap<String, Object>> productList;

    public GridBaseAdapter(Context ctx, HashMap<String, Object> imageNameArrayList,
                           ArrayList<StorageReference> storageReferenceList,
                           ArrayList<byte[]> imageBytesList,
                           HashMap<Integer, HashMap<String, Object>> productList) {

        this.ctx = ctx;
        this.imageNameArrayList = imageNameArrayList;
        this.storageReferenceList = storageReferenceList;
        this.imageBytesList = imageBytesList;
        this.productList=productList;
    }

    @Override
    public int getCount() {
        return imageNameArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageNameArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.grid_item, parent, false);
        
        ivGallery = (ImageView) itemView.findViewById(R.id.ivGallery);
        textView = (TextView) itemView.findViewById(R.id.tv);
        final long ONE_MEGABYTE = 1024 * 1024;
        // download file as a byte array
        storage = FirebaseStorage.getInstance();
//        StorageReference storageReference = storage
//                .getReferenceFromUrl("gs://uptickmarketplace-f31e9.appspot.com/product_photos/F1WBbp3sIrNuXSR3PyUtp2HylQz2/HvXuQarf9ZVjW5SD72NA.jpg");

        //StorageReference storageReference = storageReferenceList.get(position);

        HashMap<String,Object> productDetail = productList.get(position);
        byte[] imageBytes = (byte[]) productDetail.get("imageBytes");

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
                imageBytes.length);
        ivGallery.setImageBitmap(bitmap);


        textView.setText(productDetail.get("name").toString());

        return itemView;
    }

}

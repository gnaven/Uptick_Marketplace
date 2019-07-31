package com.example.android.uptick_marketplace;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

public class Sell extends AppCompatActivity {

    private TextView sell_text;
    private Toolbar Sell_toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sell);
        //Sell_toolbar = (Toolbar) findViewById(R.id.sell_toolbar);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        TextView toolbartext = toolbar.findViewById(R.id.toolbar_title);
//        toolbartext.setText("Sell");


    }
}

package com.example.android.uptick_marketplace;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

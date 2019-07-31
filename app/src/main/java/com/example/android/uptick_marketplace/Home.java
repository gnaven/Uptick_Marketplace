package com.example.android.uptick_marketplace;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.activity_home,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.home_text);
        // Send to sell page
        view.findViewById(R.id.list_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sell_activity = new Intent(getActivity(), Sell.class);
                startActivity(sell_activity);
                //Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
                //toolbartext.setText("Sell");
                //Toast.makeText(getActivity(), "Clicked Profile Button", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

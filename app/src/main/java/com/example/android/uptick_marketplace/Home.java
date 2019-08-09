package com.example.android.uptick_marketplace;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

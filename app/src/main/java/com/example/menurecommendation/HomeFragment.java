package com.example.menurecommendation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.appTitle.setText(R.string.home);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Button genButton = rootView.findViewById(R.id.generate_button);
        Button hisButton = rootView.findViewById(R.id.history_button);

        genButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
            }
        });

        hisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileFragment());
            }
        });

        return rootView;
    }

    public void replaceFragment(Fragment fragment){
        ((MainActivity)getActivity()).replaceFragment(fragment);
    }
}


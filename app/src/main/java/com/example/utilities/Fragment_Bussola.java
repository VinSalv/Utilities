package com.example.utilities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class Fragment_Bussola extends Fragment {


    public Fragment_Bussola() {
    }

    public static Fragment_Bussola newInstance(String param1, String param2) {
        return new Fragment_Bussola();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__bussola, container, false);
    }
}
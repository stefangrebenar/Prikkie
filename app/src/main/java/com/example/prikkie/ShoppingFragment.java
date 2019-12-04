package com.example.prikkie;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ShoppingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, viewGroup, false);
        TextView output= (TextView)view.findViewById(R.id.msg1);
        output.setText("Shopping Fragment");
        return view;
    }
}
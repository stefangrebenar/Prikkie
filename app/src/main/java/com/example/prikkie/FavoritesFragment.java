package com.example.prikkie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FavoritesFragment extends Fragment {
    private static FavoritesFragment m_fragment;
    public static FavoritesFragment getFragment(){
        if(m_fragment == null){
            m_fragment = new FavoritesFragment();
        }
        return m_fragment;
    }
    private FavoritesFragment(){}

    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, viewGroup, false);
        mView = view;
        return view;
    }
}

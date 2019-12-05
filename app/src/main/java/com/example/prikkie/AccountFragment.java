package com.example.prikkie;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AccountFragment extends Fragment {
    private static AccountFragment m_fragment;
    public static AccountFragment getFragment(){
        if(m_fragment == null){
            m_fragment = new AccountFragment();
        }
        return m_fragment;
    }
    private AccountFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account,container,false);
    }
}

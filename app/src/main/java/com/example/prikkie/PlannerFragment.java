package com.example.prikkie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlannerFragment extends Fragment {

    private static PlannerFragment m_fragment;

    public static PlannerFragment getFragment() {
        if (m_fragment == null) {
            m_fragment = new PlannerFragment();
        }
        return m_fragment;
    }

    private PlannerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        return view;
    }
}
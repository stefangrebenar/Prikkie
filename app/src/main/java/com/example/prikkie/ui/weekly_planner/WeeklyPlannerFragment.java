package com.example.prikkie.ui.weekly_planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.prikkie.R;

public class WeeklyPlannerFragment extends Fragment {

    private WeeklyPlannerViewModel weeklyPlannerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        weeklyPlannerViewModel =
                ViewModelProviders.of(this).get(WeeklyPlannerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_weekly_planner, container, false);
        final TextView textView = root.findViewById(R.id.text_weekly_planner);
        weeklyPlannerViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
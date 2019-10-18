package com.example.prikkie.ui.multiplayer;

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

public class MultiplayerFragment extends Fragment {

    private MultiplayerViewModel multiplayerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        multiplayerViewModel =
                ViewModelProviders.of(this).get(MultiplayerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_multiplayer, container, false);
        final TextView textView = root.findViewById(R.id.text_multiplayer);
        multiplayerViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
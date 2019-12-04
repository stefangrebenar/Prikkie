package com.example.prikkie;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setFragment(new HomeDefaultFragment(), R.id.frame_container);

        final TextView DefaultView = (TextView) view.findViewById(R.id.FirstButton);

        DefaultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                setFragment(new HomeDefaultFragment(), R.id.frame_container);
            }
        });

        TextView textButton = (TextView) view.findViewById(R.id.TextButton);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ShoppingListFragment(), R.id.frame_container);
            }
        });


        final TextView Recipe = (TextView) view.findViewById(R.id.Recipe);
        Recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new RecipeFragment(), R.id.frame_container);
            }
        });

        final TextView Search = (TextView) view.findViewById(R.id.Search);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SearchFragment(), R.id.frame_container);
            }
        });
        return view;
    }

    public void setFragment(Fragment fragment, int frame){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setWhiteBackground(Button button){

        button.setBackgroundColor(Color.BLUE);
    }
}

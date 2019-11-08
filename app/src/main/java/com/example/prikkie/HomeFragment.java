package com.example.prikkie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setFragment(new HomeDefaultFragment(), R.id.frame_container);

        ImageButton DefaultButton = (ImageButton) view.findViewById(R.id.FirstButton);
        DefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                setFragment(new HomeDefaultFragment(), R.id.frame_container);
            }
        });


        Button ShoppingList = (Button) view.findViewById(R.id.Shopping);
        ShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ShoppingFragment(), R.id.frame_container);

            }
        });

        Button Recipes = (Button) view.findViewById(R.id.Recipe);
        Recipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new RecipeFragment(), R.id.frame_container);

            }
        });

        Button Search = (Button) view.findViewById(R.id.Search);
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
}

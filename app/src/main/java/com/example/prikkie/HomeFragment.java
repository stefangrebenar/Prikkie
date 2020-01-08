package com.example.prikkie;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {
    private static HomeFragment m_fragment;
    private static Fragment m_currentFragment;
    public static HomeFragment getFragment(){
        if(m_fragment == null){
            m_fragment = new HomeFragment();
        }
        if(m_currentFragment == null){
            m_currentFragment = HomeDefaultFragment.getFragment();
        }
        return m_fragment;
    }
    private HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setFragment(m_currentFragment, R.id.frame_container);

        final TextView DefaultView = (TextView) view.findViewById(R.id.FirstButton);

        DefaultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(m_currentFragment != HomeDefaultFragment.getFragment()) {
                    m_currentFragment = HomeDefaultFragment.getFragment();
                    setFragment(m_currentFragment, R.id.frame_container);
                }
            }
        });

        TextView textButton = (TextView) view.findViewById(R.id.TextButton);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m_currentFragment != ShoppingListFragment.getFragment()) {
                    m_currentFragment = ShoppingListFragment.getFragment();
                    setFragment(m_currentFragment, R.id.frame_container);
                }
            }
        });


        final TextView Recipe = (TextView) view.findViewById(R.id.Recipe);
        Recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m_currentFragment != RecipeFragment.getFragment()) {
                    m_currentFragment = RecipeFragment.getFragment();
                    setFragment(m_currentFragment, R.id.frame_container);
                }
            }
        });

        final TextView Search = (TextView) view.findViewById(R.id.Search);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m_currentFragment != SearchFragment.getFragment()) {
                    m_currentFragment = SearchFragment.getFragment();
                    setFragment(SearchFragment.getFragment(), R.id.frame_container);
                }
            }
        });
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        String test = "";
        for(Fragment fr : getActivity().getSupportFragmentManager().getFragments()){
            test += fr.getClass().getName();
            test += "\n";
        }
        return view;
    }

    public void setFragment(Fragment fragment, int frame){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (getActivity().getSupportFragmentManager().getFragments().contains(fragment)) {
            fragmentTransaction.remove(fragment);
            fragmentTransaction.add(frame, fragment);
        }
        else {
            fragmentTransaction.replace(frame, fragment);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setWhiteBackground(Button button){
        button.setBackgroundColor(Color.BLUE);
    }
}

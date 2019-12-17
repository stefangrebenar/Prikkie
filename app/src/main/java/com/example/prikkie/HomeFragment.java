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
    public String Tab = "Home";

    private View mview;
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
                    Tab = "Home";
                    setBackground(Tab);
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
                    Tab = "Recipe";
                    setBackground(Tab);
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
                    Tab = "Search";
                    setBackground(Tab);
                    setFragment(m_currentFragment, R.id.frame_container);

                }
            }
        });

        final TextView Favorites = (TextView) view.findViewById(R.id.Favorites);
        Favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m_currentFragment != FavoritesFragment.getFragment()){
                    m_currentFragment = FavoritesFragment.getFragment();
                    Tab="Favorites";
                    setBackground(Tab);
                    setFragment(m_currentFragment, R.id.frame_container);
                }
            }
        });


//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        String test = "";
        for(Fragment fr : getActivity().getSupportFragmentManager().getFragments()){
            test += fr.getClass().getName();
            test += "\n";
        }
        mview = view;
        setBackground(Tab);
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

    public void setBackground(String tab){
        TextView home =  mview.findViewById(R.id.FirstButton);
        TextView recipe = mview.findViewById(R.id.Recipe);
        TextView search = mview.findViewById(R.id.Search);
        TextView Favorites =  mview.findViewById(R.id.Favorites);
        if(Tab != "Home")
        {
            home.setTextColor(Color.BLACK);
        }

        if(Tab != "Recipe"){
            recipe.setTextColor(Color.BLACK);
        }

        if(Tab != "Search")
        {
            search.setTextColor(Color.BLACK);
        }

        if(Tab != "Favorites")
        {
            Favorites.setTextColor(Color.BLACK);
        }
///////////////////////////////////////////////////////////////////////////////////////////////
        if(Tab == "Home"){
            home.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        if(Tab == "Recipe") {

            recipe.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        if(Tab=="Search"){
            search.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        if(Tab=="Favorites"){
            Favorites.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}

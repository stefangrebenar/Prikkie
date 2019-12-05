package com.example.prikkie;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class HomeDefaultFragment extends Fragment {
    private static HomeDefaultFragment m_fragment;
    public static HomeDefaultFragment getFragment(){
        if(m_fragment == null){
            m_fragment = new HomeDefaultFragment();
        }
        return m_fragment;
    }
    private HomeDefaultFragment(){}

    public EditText budgetID;
    public static final String USER_PREF = "USER_PREF";
    public static final String KEY_BUDGET = "KEY_BUDGET";
    public SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_default, viewGroup, false);

        Button Budget = (Button) view.findViewById(R.id.showRecipesID);
        Budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBudget(view);
            }
        });

        budgetID = (EditText) view.findViewById(R.id.budgetID);
        sp = (SharedPreferences) getContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);

        if (sp.contains(KEY_BUDGET)) {
            budgetID.setText(String.valueOf(sp.getInt(KEY_BUDGET, 0)));
        }

        return view;
    }

    public void sendBudget(View view) {

        int budget = Integer.valueOf(budgetID.getText().toString());
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_BUDGET, budget);
        editor.apply();

        setFragment(PlannerFragment.getFragment(), R.id.frame_container);
    }
    public void setFragment(Fragment fragment, int frame){
        MainActivity ma = (MainActivity) getContext();
        ma.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, fragment).commit();
    }
}
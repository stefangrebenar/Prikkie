package com.example.prikkie;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import static com.example.prikkie.App.hideKeyboardFrom;

public class HomeDefaultFragment extends Fragment {
    private static HomeDefaultFragment m_fragment;
    public static HomeDefaultFragment getFragment(){
        if(m_fragment == null){
            m_fragment = new HomeDefaultFragment();
        }
        return m_fragment;
    }
    private HomeDefaultFragment(){}

    public EditText budgetText;
    public static final String USER_PREF = "USER_PREF";
    public static final String KEY_BUDGET = "KEY_BUDGET";
    public SharedPreferences sp;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private View m_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.fragment_home_default, viewGroup, false);

        Button search = (Button) m_view.findViewById(R.id.submitBudgetButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBudget();
            }
        });
        budgetText = (EditText) m_view.findViewById(R.id.budgetHolder);
        sp = (SharedPreferences) getContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        budgetText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    budgetFilledIn();
                    return true;
                }
                return false;
            }
        });

        if (sp.contains(KEY_BUDGET)) {
            budgetText.setText(String.valueOf(sp.getFloat(KEY_BUDGET, 0)));
        }

        return m_view;
    }

    public void budgetFilledIn(){
        sendBudget();
    }

    public void sendBudget() {

        float budget = Float.parseFloat(budgetText.getText().toString());
        budget = Float.parseFloat(decimalFormat.format(budget));
        SharedPreferences.Editor editor = sp.edit();
        Log.d("TEST", "final float = " + budget);
        editor.putFloat(KEY_BUDGET, budget);
        editor.apply();

        hideKeyboardFrom(getContext(), m_view);
        setFragment(PlannerFragment.getFragment());
    }
    public void setFragment(Fragment fragment){
        MainActivity ma = (MainActivity) getContext();
        ma.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, fragment).commit();
    }
}
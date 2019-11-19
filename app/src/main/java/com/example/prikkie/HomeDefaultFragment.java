package com.example.prikkie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class HomeDefaultFragment extends Fragment {

    public EditText budgetID;
    public static final String USER_PREF = "USER_PREF";
    public static final String KEY_BUDGET = "KEY_BUDGET";
    public SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_default, viewGroup, false);


        Spinner spinner = view.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource((MainActivity)getContext(), R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    }

}
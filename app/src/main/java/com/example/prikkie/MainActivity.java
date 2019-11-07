package com.example.prikkie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText budgetID;
    public static final String USER_PREF = "USER_PREF";
    public static final String KEY_BUDGET = "KEY_BUDGET";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        budgetID = (EditText) findViewById(R.id.budgetID);
        sp = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);

        if (sp.contains(KEY_BUDGET)) {
            budgetID.setText(String.valueOf(sp.getInt(KEY_BUDGET, 0)));
        }
    }

    public void sendBudget(View view) {

        int budget = Integer.valueOf(budgetID.getText().toString());
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_BUDGET, budget);
        editor.apply();

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }
}

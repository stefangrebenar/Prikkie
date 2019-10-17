package com.example.prikkie;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    EditText budgetID;
    public static final String USER_PREF = "USER_PREF" ;
    public static final String KEY_BUDGET = "KEY_BUDGET";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        budgetID = (EditText)findViewById(R.id.budgetID);

        sp = getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);

        if (sp.contains(KEY_BUDGET)) {
            budgetID.setText(String.valueOf(sp.getInt(KEY_BUDGET, 0)));
        }
      
       Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    }


    public void sendBudget(View view){


        int budget = Integer.valueOf(budgetID.getText().toString());

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_BUDGET, budget);
        editor.apply();

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRecipeActivity(View v){
        startActivity(new Intent(MainActivity.this, RecipeApiActivity.class));
    }
}

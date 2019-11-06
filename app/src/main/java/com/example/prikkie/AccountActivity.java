package com.example.prikkie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;

import com.example.prikkie.ingredientDB.Ingredient;
import com.example.prikkie.ingredientDB.IngredientDatabaseHandler;
import com.example.prikkie.ingredients.IngredientClassificationComponent;
import com.example.prikkie.ingredients.Ingredients;
import com.example.prikkie.ingredients.presets.Presets;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {
    IngredientDatabaseHandler ingredientDb;
    public ArrayList<Ingredient> allCheckboxes = new ArrayList<Ingredient>();
    private Ingredients ingredients = new Ingredients();
    private Presets presets = new Presets();
    private int count;
    public ArrayList<String> preferences = new ArrayList<String>();
    private int Language = 1; //English = 0; Dutch = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredientDb = new IngredientDatabaseHandler(this);

        ArrayList<IngredientClassificationComponent> allPreSets = presets.GetComponents();
        ArrayList<Ingredient> allIngredients = ingredientDb.AllIngredients();
        Log.d("INGREDIENT", ingredientDb.getIngredient(1).Dutch);
//        allCheckboxes.addAll(allPreSets);
        allCheckboxes.addAll(allIngredients);
        count = allCheckboxes.size();

        // Get preferences!!!
        preferences.add("Ingredientsss");
        preferences.add("Banana");

        setContentView(R.layout.activity_account);

        GenerateCheckboxes();
    }


    private void GenerateCheckboxes() {
        GridView preferencesGrid = (GridView) findViewById(R.id.preferencesGrid);
        PreferenceAdapter preferenceAdapter = new PreferenceAdapter();
        preferencesGrid.setAdapter(preferenceAdapter);

    }

    public class PreferenceAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public PreferenceAdapter() {
            inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            {
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(
                            R.layout.preference_object, null);
                    holder.checkbox = (CheckBox) convertView.findViewById(R.id.cbIngredient);

                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.checkbox.setId(position);
                holder.checkbox.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Ingredient ingredient = allCheckboxes.get(v.getId());
                        ingredient.Checked = ((CheckBox)v).isChecked();
                        ingredientDb.updateIngredient(ingredient);
                    }
                });

                boolean checked = allCheckboxes.get(position).Checked;
                holder.checkbox.setChecked(checked);
                holder.checkbox.setText(allCheckboxes.get(position).GetLanguage(Language));
                holder.id = position;
                return convertView;
            }
    }

        class ViewHolder {
            public CheckBox checkbox;
            public int id;
        }
    }
}
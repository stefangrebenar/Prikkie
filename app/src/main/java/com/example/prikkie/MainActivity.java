package com.example.prikkie;
import android.content.Intent;
import android.os.Bundle;

import com.example.prikkie.Api.recipe_api.Recipe;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    RecipeFragment RecipeFragment;
    SearchFragment SearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_planner:
                            selectedFragment = new PlannerFragment();
                            break;
                        case R.id.nav_account:
                            selectedFragment = new AccountFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    public void showRecipe(View v){
        RecipeFragment.showRecipe(v);
    }

    public void updateRecipes(ArrayList<Recipe> recipes) {
        RecipeFragment.updateRecipes(recipes);
    }

    public void getProductsButton(View v){
        SearchFragment.getProductsButton(v);
    }

    public void onRecipeActivity(View v){
        startActivity(new Intent(MainActivity.this, RecipeApiActivity.class));
    }

    public void onIngredientActivity(View v){
        startActivity(new Intent(MainActivity.this, IngredientApiActivity.class));
    }
}

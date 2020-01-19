package com.example.prikkie;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prikkie.ingredientDB.IngredientDatabaseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IngredientDatabaseHandler ingredientDb = new IngredientDatabaseHandler(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setLogo(R.drawable.prikkie_logo);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,HomeFragment.getFragment()).commit();

    }

    @Override
    public void onBackPressed(){
        if(HomeDefaultFragment.getFragment().isVisible()){
            this.finishAffinity();
        } else if(RecipeDetails.getFragment().isVisible()){
            RecipeDetails.getFragment().CloseView();
        } else if(WeeklyPlannerFragment.getFragment().isVisible()){
            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, HomeFragment.getFragment()).commit();
        } else if(ShoppingListFragment.getFragment().isVisible()){
            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, HomeFragment.getFragment()).commit();
        }
        else{
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = HomeFragment.getFragment();
                            break;
                        case R.id.nav_planner:
                            selectedFragment = WeeklyPlannerFragment.getFragment();
                            break;
                        case R.id.nav_shoppinglist:
                            selectedFragment = ShoppingListFragment.getFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

}

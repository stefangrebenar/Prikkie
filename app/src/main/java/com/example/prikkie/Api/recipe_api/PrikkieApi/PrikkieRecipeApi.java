package com.example.prikkie.Api.recipe_api.PrikkieApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.App;
import com.example.prikkie.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PrikkieRecipeApi{
    private final String urlQuery = App.getContext().getString(R.string.prikkie_api);
    private final String USER_PREF = "USER_PREF";
    public SharedPreferences sp = (SharedPreferences) App.getContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);

    public ArrayList<Recipe> getRecipesByName(String name){
        ArrayList<Recipe> results = new ArrayList<Recipe>();
        // call api. query should be with %name% if I remember it right.
        return results;
    }

    public int getAmountOfRecipes(){
        PrikkieAmountOfRecipesAsync apiAsync = new PrikkieAmountOfRecipesAsync();
        apiAsync.execute();

        try {
            return apiAsync.get(10, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<Recipe> getRandomRecipes(int[] checkedIds){
        ArrayList<Recipe> results = new ArrayList<Recipe>();
        if(sp.contains(USER_PREF)){
            // call api. query should exclude ingredient ids and checked recipe ids
            Log.d("TEST", "Contains USER PREFERENCES");
        }
        else {
            // call api. query should exclude the checked recipe ids.
            PrikkieRandomRecipeAsync recipeAsync = new PrikkieRandomRecipeAsync();
            recipeAsync.checkedIds = checkedIds;
            recipeAsync.execute();
            try {
                results = recipeAsync.get(10, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        if(results != null) {
            Log.d("TEST", "Result = " + results.toString());
            //Todo Create different async calls.
            return results;
        }
        return null;
    }
}

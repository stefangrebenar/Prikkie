package com.example.prikkie.Api.recipe_api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.prikkie.App;
import com.example.prikkie.R;

import java.util.ArrayList;

public class PrikkieRecipeApi{
    private String urlQuery = App.getContext().getString(R.string.prikkie_api);
    public static final String USER_PREF = "USER_PREF";
    public SharedPreferences sp = (SharedPreferences) App.getContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);

    public ArrayList<Recipe> getRecipesByName(String name){
        ArrayList<Recipe> results = new ArrayList<Recipe>();
        // call api. query should be with %name% if I remember it right.
        return results;
    }

    public ArrayList<Recipe> getRandomRecipes(int[] checkedIds){
        ArrayList<Recipe> results = new ArrayList<Recipe>();
        if(sp.contains(USER_PREF)){
            // call api. query should exclude ingredient ids and checked recipe ids
        }
        else {
            // call api. query should exclude the checked recipe ids.
        }
        //Todo Create different async calls.
        return results;
    }
}

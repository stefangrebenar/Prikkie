package com.example.prikkie.Api.recipe_api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.prikkie.R;
import com.example.prikkie.RecipeApiActivity;
import com.example.prikkie.ingredientDB.Ingredient;
import com.example.prikkie.ingredientDB.IngredientDatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class RecipePuppy extends RecipeApi {

    public RecipePuppy(Context current) {
        context = current;
    }

    @Override
    public void getRecipeFromApi(String keywords, String includedIngredients, String excludedIngredients) {
        // Initialize variables
        recipes = new ArrayList<Recipe>();
        request = Volley.newRequestQueue(context);
        //Url to receive recipes
        String url = context.getResources().getString(R.string.recipe_puppy_api);
        //Construct url
        url = constructUrl(url, keywords, includedIngredients, excludedIngredients);
        Log.d("TEST", url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonRecipe = jsonArray.getJSONObject(i);
                        Recipe recipe = new Recipe();
                        recipe.title = jsonRecipe.getString("title");
                        recipe.imagePath = jsonRecipe.getString("thumbnail");
                        recipe.href = jsonRecipe.getString("href");
                        String ingredients = jsonRecipe.getString("ingredients");
                        recipe.ingredients = new ArrayList<String>(Arrays.asList(ingredients.split("\\s*,\\s*")));
                        recipes.add(recipe);
                    }

                    // Bad code workaround for async methode
                    RecipeApiActivity recipeActivity = (RecipeApiActivity) context;
                    recipeActivity.updateRecipes(recipes);
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        this.request.add(request);
    }

    private String prepareExcludesForUrl(String excludes, int includeLength){
        String result = "";

        IngredientDatabaseHandler ingredientDb = new IngredientDatabaseHandler(context);
        for(Ingredient ingredient : ingredientDb.GetCheckedIngredients()){
            if(excludes.length() == 0){
                excludes += ingredient.English;
                continue;
            }
            excludes += "," + ingredient.English;
        }
        if(excludes.length() > 0) {//If there are ingredients to exclude
            //Remove all spaces
            excludes = excludes.replaceAll("\\s+", "");
            if(includeLength > 0)   //If there are ingredients to include, separate them by ,
            {
                result += ",";
            }
            //Add - before ingredients to exclude them
            result += "-";
            //Add - after ,
            excludes = excludes.replaceAll(",", ",-");
            excludes = AddPreferences(excludes);
        }
        //Add to url
        result += excludes;
        return result;
    }

    @Override
    protected String AddPreferences(String currentString){
        // Get preferences

//        if(preferences.length() == 0) {
//            return "";
//        }
//
//        if (currentString.length() > 0) {
//            currentString += ",-" + preferences[0];
//        } else {
//            currentString += "-" + preferences[0];
//        }
//
//        //for all preferences
//        for(int i = 1; i < preferences.length(); i++){
//            currentString += ",-" + preferences[i];
//        }

        return currentString;
    }

    private String constructUrl(String url, String keywords, String includedIngredients, String excludedIngredients){
        //If there are any ingredient preferences, add the pre-fix to the url
        String totalExcludes = prepareExcludesForUrl(excludedIngredients, includedIngredients.length());
        if(includedIngredients.length()+totalExcludes.length() > 0){
            url += context.getResources().getString(R.string.ingredient);
        }

        try {
            //Remove all spaces
            includedIngredients = includedIngredients.replaceAll("\\s+", "");
            //Add to url
            url += includedIngredients;
        }
        catch(Exception e){
            Log.d("ERROR", "Can't add ingredients to url");
        }

        url += totalExcludes;

        //Add recipe to url
        if(includedIngredients.length()+totalExcludes.length()>0){
            url += "&";
        }
        url += context.getResources().getString(R.string.recipe_keyword);

        //Replace Spaces with _
        keywords.replaceAll(" ", "_");
        //Add keywords to url
        url+= keywords;
        return url;
    }
}

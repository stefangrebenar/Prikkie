package com.example.prikkie.Api.recipe_api.PrikkieApi;

import android.os.AsyncTask;
import android.util.Log;

import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.App;
import com.example.prikkie.R;
import com.example.prikkie.RecipeFragment;
import com.example.prikkie.ingredientDB.Ingredient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PrikkieRecipesAsync extends AsyncTask<String, Void, ArrayList<Recipe>> {
    private String urlQuery = App.getContext().getString(R.string.prikkie_api) + App.getContext().getString(R.string.prikkie_recipes);
    private ArrayList<Recipe> recipes;
    public String name = "";
    public String includes = "";
    public String excludes = "";
    public int page;

    private String formatIngredients(String includes, String excludes){
        // Format strings
        // Remove extra spaces
        includes = includes.replaceAll(",\\s+", ",");
        excludes = excludes.replaceAll(",\\s+", ",-");
        includes = includes.replaceAll("\\s+", "%20");
        excludes = excludes.replaceAll("\\s+", "%20");
        // Add ,- before excludes if nessecairy
        if(excludes.length() > 0) {
            excludes = "-" + excludes;
            if (includes.length() > 0) {
                excludes = "," + excludes;
            }
        }
        return includes+excludes;
    }

    @Override
    protected ArrayList<Recipe> doInBackground(String... strings) {
        try{
            // Build url query
            // Add name
            urlQuery += "?" + App.getContext().getString(R.string.prikkie_recipe_name)+name;
            // Add ingredients
            String ingredients = formatIngredients(includes, excludes);
            urlQuery += "&" + App.getContext().getString(R.string.prikkie_ingredient)+ingredients;
            // Add Page
            urlQuery += "&" + App.getContext().getString(R.string.prikkie_page)+page;

            // Preform request
            Log.d("TEST", urlQuery);
            HttpGet httpGet = new HttpGet(urlQuery);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            // If response is Ok 200
            if(status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(data);
                if (recipes == null){
                    recipes = new ArrayList<Recipe>();
                }
                if(jsonObject.has("data")){
                    JSONArray recipes = jsonObject.getJSONArray("data");
                    int recipeLength = recipes.length();
                    // Read JSON-response
                    // For each recipe in the response, create a representing Recipe object and add it to the recipes-List
                    for (int j = 0; j < recipeLength; j++) {
                        JSONObject base = recipes.getJSONObject(j);
                        Recipe recipe = new Recipe();
                        if (base.has("recipe_id")) {
                            recipe.id = base.getInt("recipe_id");
                        }
                        if (base.has("title")) {
                            recipe.title = base.getString("title");
                        }
                        if (base.has("description")) {
                            recipe.description = base.getString("description");
                        }
                        if (base.has("method")) {
                            recipe.method = base.getString("method");
                        }
                        if (base.has("persons")) {
                            recipe.persons = base.getInt("persons");
                        }
                        if (base.has("image")) {
                            recipe.imagePath = base.getString("image");
                        }
                        if (base.has("ingredients")) {
                            JSONArray ingredientArray = base.getJSONArray("ingredients");
                            int amountOfIngredients = ingredientArray.length();
                            ArrayList<Ingredient> recipeIngredients = new ArrayList<Ingredient>();

                            // Add all ingredients
                            for (int k = 0; k < amountOfIngredients; k++) {
                                Ingredient ingredient = new Ingredient();
                                JSONObject ingredientObject = ingredientArray.getJSONObject(k);
                                if (ingredientObject.has("ingredient_id")) {
                                    ingredient.Id = ingredientObject.getInt("ingredient_id");
                                }
                                if (ingredientObject.has("name")) {
                                    ingredient.Dutch = ingredientObject.getString("name");
                                }
                                if (ingredientObject.has("amount")) {
                                    ingredient.amount = ingredientObject.getString("amount");
                                }
                                if (ingredientObject.has("unit")) {
                                    ingredient.unit = ingredientObject.getString("unit");
                                }
                                if (ingredientObject.has("taxonomy")) {
                                    ingredient.Taxonomy = ingredientObject.getString("taxonomy");
                                }
                                recipeIngredients.add(ingredient);
                            }
                            recipe.ingredients = recipeIngredients;
                        }
                        this.recipes.add(recipe);
//                        Collections.shuffle(this.recipes);
                    }
                }
                if(jsonObject.has("last_page")){
                    RecipeFragment rf = RecipeFragment.getFragment();
                    if(rf.isVisible()){
                        rf.lastPage = jsonObject.getInt("last_page");
                    }
                }
                return recipes;
            }
            // no OK response from server
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}

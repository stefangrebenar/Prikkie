package com.example.prikkie.Api.recipe_api.PrikkieApi;

import android.os.AsyncTask;

import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.App;
import com.example.prikkie.R;
import com.example.prikkie.ingredientDB.Ingredient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.ArrayList;

public class PrikkieRandomRecipeAsync extends AsyncTask<String, Void, ArrayList<Recipe>> {
    private final String urlQuery = App.getContext().getString(R.string.prikkie_api) + App.getContext().getString(R.string.prikkie_recipes);
    private ArrayList<Recipe> recipes;
    public int[] checkedIds;


    @Override
    protected ArrayList<Recipe> doInBackground(String... strings) {
        try{
            // Preform request
            HttpPost httppost = new HttpPost(urlQuery);
            StringEntity stringEntity = new StringEntity(checkedIds.toString());
            httppost.setEntity(stringEntity);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);        // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            // If response is Ok 200
            if(status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                JSONArray recipeArray = new JSONArray(data);

                int length = recipeArray.length();
                if (recipes == null){
                    recipes = new ArrayList<Recipe>();
                }
                // Read JSON-response
                // For each recipe in the response, create a representing Recipe object and add it to the recipes-List
                for(int i = 0; i < length; i++){
                    JSONObject base = recipeArray.getJSONObject(i);

                    Recipe recipe = new Recipe();
                    if(base.has("recipe_id")){
                        recipe.id = base.getInt("recipe_id");
                    }
                    if(base.has("title")){
                        recipe.title = base.getString("title");
                    }
                    if(base.has("description")){
                        recipe.description = base.getString("description");
                    }
                    if(base.has("method")){
                        recipe.method = base.getString("method");
                    }
                    if(base.has("persons")){
                        recipe.persons = base.getInt("persons");
                    }
                    if(base.has("image")){
                        recipe.imagePath = base.getString("image");
                    }
                    if(base.has("ingredients")){
                        JSONArray ingredientArray = base.getJSONArray("ingredients");
                        int amountOfIngredients = ingredientArray.length();
                        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

                        // Add all ingredients
                        for(int j = 0; j < amountOfIngredients; j++){
                            Ingredient ingredient = new Ingredient();
                            JSONObject ingredientObject = ingredientArray.getJSONObject(j);
                            if(ingredientObject.has("ingredient_id")){
                                ingredient.Id = ingredientObject.getInt("ingredient_id");
                            }
                            if(ingredientObject.has("name")){
                                ingredient.Dutch = ingredientObject.getString("name");
                            }
                            if(ingredientObject.has("taxonomy")){
                                ingredient.Taxonomy = ingredientObject.getString("taxonomy");
                            }
                            ingredients.add(ingredient);
                        }
                        recipe.ingredients = ingredients;
                    }
                    recipes.add(recipe);
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

package com.example.prikkie;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.AHAPIAsync;
import com.example.prikkie.Api.IngredientApi.Product;
import com.example.prikkie.Api.recipe_api.PrikkieApi.PrikkieRecipeApi;
import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.ingredientDB.Ingredient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.prikkie.App.hideKeyboardFrom;


public class WeeklyPlannerFragment extends Fragment {

    public View m_view;
    private static WeeklyPlannerFragment m_fragment;
    private RecyclerView resultRecycler;
    private ArrayList<ExampleItem> resultItems = new ArrayList<>();
    private RecipeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button submitButton;
    private EditText budgetHolder;
    private EditText daysHolder;
    private ProductAsync lastTask;
    private ProgressBar m_loader;

    public static WeeklyPlannerFragment getFragment() {
        if (m_fragment == null) {
            m_fragment = new WeeklyPlannerFragment();
        }
        return m_fragment;
    }

    private WeeklyPlannerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.fragment_planner, viewGroup, false);

        m_loader = (ProgressBar) m_view.findViewById(R.id.progressBarPlannerFragment);
        resultRecycler = (RecyclerView) m_view.findViewById(R.id.recipeList);
        submitButton = m_view.findViewById(R.id.submitBudgetButton);
        budgetHolder = m_view.findViewById(R.id.budgetHolder);
        daysHolder = m_view.findViewById(R.id.daysHolder);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!budgetHolder.getText().toString().isEmpty() && !daysHolder.getText().toString().isEmpty()) {
                    resultItems.clear();
                    if (lastTask != null) {
                        lastTask.cancel(true);
                    }
                    m_loader.setVisibility(View.VISIBLE);
                    lastTask = new ProductAsync(Float.parseFloat(budgetHolder.getText().toString()), Integer.parseInt(daysHolder.getText().toString()));
                    lastTask.execute();
                    Toast msg = Toast.makeText(getContext(), "Recepten worden opgehaald", Toast.LENGTH_SHORT);
                    hideKeyboardFrom(getContext(), m_view);
                    msg.show();
                } else {
                    Toast msg = Toast.makeText(getContext(), "Vul een budget en aantal dagen in", Toast.LENGTH_LONG);
                    msg.show();
                }

            }
        });

        buildRecyclerView();


        int[] i = new int[0];


        return m_view;
    }

    public void buildRecyclerView() {
        resultRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager((MainActivity) getContext());
        mAdapter = new RecipeAdapter(resultItems);

        resultRecycler.setLayoutManager(mLayoutManager);
        resultRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String title = resultItems.get(position).getTopText();
                ProgressBar loader = (ProgressBar)mLayoutManager.findViewByPosition(position).findViewById(R.id.progressBarWeeklyItem);
                loader.setVisibility(View.VISIBLE);
                Toast msg = Toast.makeText(getContext(), title + " wordt vervangen voor een ander recept", Toast.LENGTH_SHORT);
                msg.show();
                new RefreshAsync(Float.parseFloat(budgetHolder.getText().toString()), Integer.parseInt(daysHolder.getText().toString()), loader, position, new int[0]).execute();
            }
        });
    }


    ///
    ///
    /// Get recipes for x amount of days
    ///
    ///
    public class ProductAsync extends AsyncTask<Void, Void, ArrayList<Recipe>> {
        private float budget;
        private int amountOfDays;
        private Map<String, Double> ingredientPrice = new HashMap<String, Double>();
        private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        final PrikkieRecipeApi api = new PrikkieRecipeApi();

        public ProductAsync(float budget, int amountOfDays) {
            this.budget = budget;
            this.amountOfDays = amountOfDays;
        }

        @Override
        protected ArrayList<Recipe> doInBackground(Void... voids) {

            ArrayList<Recipe> recipes = getRecipesByBudget();

            return recipes;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            for (Recipe recipe : recipes) {
                resultItems.add(new ExampleItem(recipe.imagePath, recipe.title, String.format(Locale.GERMAN, "%.2f", recipe.price)));
            }
            mAdapter.notifyDataSetChanged();
            m_loader.setVisibility(View.INVISIBLE);

            super.onPostExecute(recipes);
        }

        public ArrayList<Recipe> getRecipesByBudget() {
            int amountOfRecipes = getAmountofRecipes(); // get from api (Maybe without the excluded recipes)
            int amountOfCheckedRecipes = 0;
            int[] checkedRecipes = new int[amountOfRecipes];
            ArrayList<Recipe> finalRecipes = new ArrayList<Recipe>();
            ArrayList<Ingredient> excludedIngredients = new ArrayList<Ingredient>();
            // Api get preferences?

            Log.d("planner123", "Amount of recipes" + amountOfRecipes);
            do {
                Log.i("planner123", "ignored recipeIds" + Arrays.toString(checkedRecipes));
                ArrayList<Recipe> recipes = getRandomRecipes2(checkedRecipes);
                if (recipes == null) {
                    Log.d("planner123", "didn't get any recipes");
                    return null;
                }
                Log.d("planner123", "Amount of recipes found" + recipes.size());

                for (Recipe recipe : recipes) {
                    double recipePrice = getPriceForIngredients(recipe.ingredients);
                    Log.d("planner123", recipe.title + " = " + recipePrice + " id:" + recipe.id);
                    if (recipePrice <= budget / amountOfDays) {
                        recipe.price = recipePrice;
                        finalRecipes.add(recipe);
                        checkedRecipes[amountOfCheckedRecipes] = recipe.id;
                        amountOfCheckedRecipes++;
                        break;
                    }
                    if (checkedRecipes.length > 0) {
                        checkedRecipes[amountOfCheckedRecipes] = recipe.id;
                    }
                    amountOfCheckedRecipes++;
                }
            } while (finalRecipes.size() < amountOfDays && amountOfCheckedRecipes < amountOfRecipes - 1);

            Log.i("planner123", "returning final recipe");
            return finalRecipes;
        }

        public ArrayList<Recipe> getRandomRecipes(ArrayList<Ingredient> excludedIngredients, int[] checkedRecipes) {
            ArrayList<Recipe> recipes;

            recipes = api.getRandomRecipes(checkedRecipes);
            // Todo when implementing filters
            // recipes = GetRandom(excludedIngredients, checkedRecipes);

            return recipes;
        }

        private double getPriceForIngredients(ArrayList<Ingredient> ingredients) {
            double price = 0.0;
            for (Ingredient ingredient : ingredients) {
                if (ingredientPrice.containsKey(ingredient.Dutch)) {
                    price += ingredientPrice.get(ingredient.Dutch);
                    continue;
                }

                AHAPI api = new AHAPI(1, listener);
                api.setQuery(ingredient.Dutch);
                api.setTaxonomy(ingredient.Taxonomy);
                api.orderBy(AHAPI.orderBy.ASC);
                List<Product> products = api.getProducts(getContext());

                double minPrice = Double.POSITIVE_INFINITY;
                if (products == null) {

                    Log.i("planner123", ingredient.Dutch + ", " + ingredient.Taxonomy);
                    return minPrice;
                }
                minPrice = products.get(0).price;
                ingredientPrice.put(ingredient.Dutch, minPrice);
                price += minPrice;
            }

            return price;
        }

        public int getAmountofRecipes() {
            String urlQuery = App.getContext().getString(R.string.prikkie_api) + "totalrecipeamount";


            // Preform request
            HttpGet httppost = new HttpGet(urlQuery);
            HttpClient httpclient = new DefaultHttpClient();
            try {
                HttpResponse response = httpclient.execute(httppost);        // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                // If response is Ok 200
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    return Integer.parseInt(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        public ArrayList<Recipe> getRandomRecipes2(int[] checkedIds) {
            String urlQuery = App.getContext().getString(R.string.prikkie_api) + App.getContext().getString(R.string.prikkie_randomRecipes);
            ArrayList<Recipe> recipes = new ArrayList<Recipe>();

            try {
                String entityString = "";
                for (int i = 0; i < checkedIds.length; i++) {
                    if (i == 0) {
                        entityString += checkedIds[i];
                    } else {
                        entityString += (", " + checkedIds[i]);
                    }
                }
                // Preform request
                HttpPost httppost = new HttpPost(urlQuery);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add((new BasicNameValuePair("stringdata", entityString)));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);        // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                // If response is Ok 200
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    JSONArray recipeArray = new JSONArray(data);

                    int length = recipeArray.length();
                    if (recipes == null) {
                        recipes = new ArrayList<Recipe>();
                    }
                    // Read JSON-response
                    // For each recipe in the response, create a representing Recipe object and add it to the recipes-List
                    for (int i = 0; i < length; i++) {
                        JSONObject base = recipeArray.getJSONObject(i);

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
                            ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

                            // Add all ingredients
                            for (int j = 0; j < amountOfIngredients; j++) {
                                Ingredient ingredient = new Ingredient();
                                JSONObject ingredientObject = ingredientArray.getJSONObject(j);
                                if (ingredientObject.has("ingredient_id")) {
                                    ingredient.Id = ingredientObject.getInt("ingredient_id");
                                }
                                if (ingredientObject.has("name")) {
                                    ingredient.Dutch = ingredientObject.getString("name");
                                }
                                if (ingredientObject.has("taxonomy")) {
                                    ingredient.Taxonomy = ingredientObject.getString("taxonomy");
                                }
                                ingredients.add(ingredient);
                            }
                            recipe.ingredients = ingredients;
                        }
                        recipes.add(recipe);
                        Collections.shuffle(recipes);
                    }

                    return recipes;
                }
                // no OK response from server
            } catch (Exception e) {
                e.printStackTrace();
            }

            return recipes;
        }
    }

    public class RefreshAsync extends AsyncTask<Void, Void, Recipe> {
        private ProgressBar loader;
        private float budget;
        private int amountOfDays;
        private int index;
        private int[] checkedRecipes;
        private Map<String, Double> ingredientPrice = new HashMap<String, Double>();
        private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        final PrikkieRecipeApi api = new PrikkieRecipeApi();

        public RefreshAsync(float budget, int amountOfDays, ProgressBar loader, int index, int[] checkedRecipes) {
            this.budget = budget;
            this.amountOfDays = amountOfDays;
            this.index = index;
            this.checkedRecipes = checkedRecipes;
            this.loader = loader;
        }

        @Override
        protected Recipe doInBackground(Void... voids) {

            Recipe recipes = getRecipesByBudget();

            return recipes;
        }

        @Override
        protected void onPostExecute(Recipe recipe) {
//            for (Recipe recipe: recipes) {
//                resultItems.add(new ExampleItem(recipe.imagePath, recipe.title, String.format(Locale.GERMAN,"%.2f", recipe.price)));
//            }
            resultItems.set(index, new ExampleItem(recipe.imagePath, recipe.title, String.format(Locale.GERMAN, "%.2f", recipe.price)));
            mAdapter.notifyDataSetChanged();
            loader.setVisibility(View.INVISIBLE);
            super.onPostExecute(recipe);
        }

        public Recipe getRecipesByBudget() {
            int amountOfRecipes = getAmountofRecipes(); // get from api (Maybe without the excluded recipes)
            int amountOfCheckedRecipes = 0;
            int[] checkedRecipes = new int[amountOfRecipes];
            Recipe finalRecipe = null;
            ArrayList<Ingredient> excludedIngredients = new ArrayList<Ingredient>();
            // Api get preferences?

            Log.d("planner123", "Amount of recipes" + amountOfRecipes);
            do {
                Log.i("planner123", "ignored recipeIds" + Arrays.toString(checkedRecipes));
                ArrayList<Recipe> recipes = getRandomRecipes2(this.checkedRecipes);
                if (recipes == null) {
                    Log.d("planner123", "didn't get any recipes");
                    return null;
                }
                Log.d("planner123", "Amount of recipes found" + recipes.size());

                for (Recipe recipe : recipes) {
                    double recipePrice = getPriceForIngredients(recipe.ingredients);
                    Log.d("planner123", recipe.title + " = " + recipePrice + " id:" + recipe.id);
                    if (recipePrice <= budget / amountOfDays) {
                        recipe.price = recipePrice;
                        checkedRecipes[amountOfCheckedRecipes] = recipe.id;
                        finalRecipe = recipe;
                        amountOfCheckedRecipes++;
                        break;
                    }
                    if (checkedRecipes.length > 0) {
                        checkedRecipes[amountOfCheckedRecipes] = recipe.id;
                    }
                    amountOfCheckedRecipes++;
                }
            } while (finalRecipe == null && amountOfCheckedRecipes < amountOfRecipes - 1);

            Log.i("planner123", "returning final recipe");
            return finalRecipe;
        }

        public ArrayList<Recipe> getRandomRecipes(ArrayList<Ingredient> excludedIngredients, int[] checkedRecipes) {
            ArrayList<Recipe> recipes;

            recipes = api.getRandomRecipes(checkedRecipes);
            // Todo when implementing filters
            // recipes = GetRandom(excludedIngredients, checkedRecipes);

            return recipes;
        }

        private double getPriceForIngredients(ArrayList<Ingredient> ingredients) {
            double price = 0.0;
            for (Ingredient ingredient : ingredients) {
                if (ingredientPrice.containsKey(ingredient.Dutch)) {
                    price += ingredientPrice.get(ingredient.Dutch);
                    continue;
                }

                AHAPI api = new AHAPI(1, listener);
                api.setQuery(ingredient.Dutch);
                api.setTaxonomy(ingredient.Taxonomy);
                api.orderBy(AHAPI.orderBy.ASC);
                List<Product> results = api.getProducts(getContext());


                double minPrice = Double.POSITIVE_INFINITY;
                if (results == null) {

                    Log.i("planner123", ingredient.Dutch + ", " + ingredient.Taxonomy);
                    return minPrice;
                }
                minPrice = results.get(0).price;
                Product product = results.get(0);
                 minPrice = product.price;
                ingredientPrice.put(ingredient.Dutch, minPrice);
                price += minPrice;

            }
            return price;
        }

        public int getAmountofRecipes() {
            String urlQuery = App.getContext().getString(R.string.prikkie_api) + "totalrecipeamount";


            // Preform request
            HttpGet httppost = new HttpGet(urlQuery);
            HttpClient httpclient = new DefaultHttpClient();
            try {
                HttpResponse response = httpclient.execute(httppost);        // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                // If response is Ok 200
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    return Integer.parseInt(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        public ArrayList<Recipe> getRandomRecipes2(int[] checkedIds) {
            String urlQuery = App.getContext().getString(R.string.prikkie_api) + "randomrecipe";//App.getContext().getString(R.string.prikkie_recipes);
            ArrayList<Recipe> recipes = new ArrayList<Recipe>();
            try {
                String entityString = "";
                for (int i = 0; i < checkedIds.length; i++) {
                    if (i == 0) {
                        entityString += checkedIds[i];
                    } else {
                        entityString += (", " + checkedIds[i]);
                    }
                }
                // Preform request
                HttpPost httppost = new HttpPost(urlQuery);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add((new BasicNameValuePair("stringdata", entityString)));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);        // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();
                // If response is Ok 200
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    JSONArray recipeArray = new JSONArray(data);

                    int length = recipeArray.length();
                    if (recipes == null) {
                        recipes = new ArrayList<Recipe>();
                    }
                    // Read JSON-response
                    // For each recipe in the response, create a representing Recipe object and add it to the recipes-List
                    for (int i = 0; i < length; i++) {
                        JSONObject base = recipeArray.getJSONObject(i);

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
                            ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

                            // Add all ingredients
                            for (int j = 0; j < amountOfIngredients; j++) {
                                Ingredient ingredient = new Ingredient();
                                JSONObject ingredientObject = ingredientArray.getJSONObject(j);
                                if (ingredientObject.has("ingredient_id")) {
                                    ingredient.Id = ingredientObject.getInt("ingredient_id");
                                }
                                if (ingredientObject.has("name")) {
                                    ingredient.Dutch = ingredientObject.getString("name");
                                }
                                if (ingredientObject.has("taxonomy")) {
                                    ingredient.Taxonomy = ingredientObject.getString("taxonomy");
                                }
                                ingredients.add(ingredient);
                            }
                            recipe.ingredients = ingredients;
                        }
                        recipes.add(recipe);
                        Collections.shuffle(recipes);
                    }

                    return recipes;
                }
                // no OK response from server
            } catch (Exception e) {
                e.printStackTrace();
            }

            return recipes;
        }
    }
    AHAPI.onResultLoadedListener listener = new AHAPI.onResultLoadedListener() {
        @Override
        public void onResultLoaded(List<Product> products) {
//            resultItems.clear();
//
//            String text = "";
//            for (Product product:products) {
//                resultItems.add(new ExampleItem(product.imgURL, product.name, Double.toString(product.price)));
//            }
//            mAdapter.notifyDataSetChanged();
        }
    };
}
package com.example.prikkie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.AHAPIAsync;
import com.example.prikkie.Api.IngredientApi.Product;
import com.example.prikkie.Api.recipe_api.PrikkieApi.PrikkieRecipeApi;
import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.ingredientDB.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class PlannerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_planner,container,false);

        Log.d("TEST", "Making view...");
        RecipeThread rt = new RecipeThread(this, view);
        Thread t = new Thread(rt);
        t.start();
        return view;
    }

}
class RecipeThread implements Runnable {
    private int budget;
    private Map<String, Double> ingredientPrice = new HashMap<String, Double>();
    private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    final PrikkieRecipeApi api = new PrikkieRecipeApi();

    public static final String USER_PREF = "USER_PREF";
    public static final String KEY_BUDGET = "KEY_BUDGET";
    public SharedPreferences sp;

    private View view;

    public Recipe recipe;
    private ImageView recipePicture;
    private TextView recipeTitle;
    private TextView ingredientList;
    private TextView recipePreperations;

    public RecipeThread(Fragment fragment, View view){
        sp = (SharedPreferences) fragment.getContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        this.view = view;
    }

    @Override
    public void run() {
        Log.d("TEST", "Starting background tasks");
        recipe = getRecipesByBudget();

        ((MainActivity) view.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recipePreperations = view.findViewById(R.id.recipePreparations);
                if (recipe != null) {
                    String ingredientsListed = "";
                    recipePicture = view.findViewById(R.id.generatedRecipePicture);
                    ingredientList = view.findViewById(R.id.recipeIngredientList);
                    recipeTitle = view.findViewById(R.id.recipeTitle);
                    // recipePicture.setImageBitmap(recipe.bitmap);
                    for (
                            Ingredient ingredient : recipe.ingredients) {
                        ingredientsListed += "+ " + ingredient.GetLanguage(1) + "\n";
                    }

                    Picasso.get().load(recipe.imagePath).resize(recipePicture.getWidth(), recipePicture.getHeight()).into(recipePicture);
                    recipeTitle.setText(recipe.title);
                    ingredientList.setText(ingredientsListed);
                    recipePreperations.setText(recipe.method);
                } else {
                    recipePreperations.setText("Failed to load recipe");
                }
            }
        });
    }
    public Recipe getRecipesByBudget(){
        if (!sp.contains(KEY_BUDGET)) {
            Log.d("TEST", "Budget not found");
            return null; // budget not found
        }
        budget = sp.getInt(KEY_BUDGET, 0);
        int amountOfRecipes = getAmountOfRecipes(); // get from api (Maybe without the excluded recipes)
        int amountOfCheckedRecipes = 0;
        int[] checkedRecipes = new int[amountOfRecipes];
        Recipe finalRecipe = null;
        ArrayList<Ingredient> excludedIngredients = new ArrayList<Ingredient>();
        // Api get preferences?
        do{
            ArrayList<Recipe> recipes = getRandomRecipes(excludedIngredients, checkedRecipes);
            if(recipes == null){
                return null;
            }
            for(Recipe recipe : recipes){
                double recipePrice = getPriceForIngredients(recipe.ingredients);
//                Log.d("TEST", "Recipe: "+ recipe.title + " = " + recipePrice);
                if(recipePrice <= budget){
//                    Log.d("TEST", "Recipe costs less than " + budget);
                    finalRecipe = recipe;
                    break;
                }
                if(checkedRecipes.length > 0) {
                    checkedRecipes[amountOfCheckedRecipes] = recipe.id;
                }
                amountOfCheckedRecipes++;
            }
        }while(finalRecipe == null && amountOfCheckedRecipes < amountOfRecipes-1);

        return finalRecipe;
    }

    private ArrayList<Recipe> getTestRecipes(){
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient(0, "tomato", "tomaat", false));
        ingredients.add(new Ingredient(0, "cheese", "kaas", false));
        ingredients.add(new Ingredient(0, "pesto", "pesto", false));

        ArrayList<Ingredient> ingredients2 = new ArrayList<Ingredient>();
        ingredients2.add(new Ingredient(0, "toast", "brood", false));
        ingredients2.add(new Ingredient(0, "cheese", "kaas", false));
        ingredients2.add(new Ingredient(0, "ham", "ham", false));

        ArrayList<Ingredient> ingredients3 = new ArrayList<Ingredient>();
        ingredients3.add(new Ingredient(0, "toast", "brood", false));
        ingredients3.add(new Ingredient(0, "tomato", "tomaat", false));
        ingredients3.add(new Ingredient(0, "cheese", "kaas", false));
        ingredients3.add(new Ingredient(0, "ham", "ham", false));
        ingredients3.add(new Ingredient(0, "pesto", "pesto", false));

        ArrayList<Ingredient> ingredients4 = new ArrayList<Ingredient>();
        ingredients4.add(new Ingredient(0, "toast", "brood", false));

        recipes.add(new Recipe("Ultimate sandwich", ingredients3, "Just DO IT!!!!"));
        recipes.add(new Recipe("Caprise", ingredients, "Melt da cheese"));
        recipes.add(new Recipe("Toast", ingredients2, "Toast the toast and melt da cheese"));
        recipes.add(new Recipe("Droog brood bitch", ingredients4, "Be rich"));

        return recipes;
    }

    private int getAmountOfRecipes(){
        return api.getAmountOfRecipes();
    }

    public ArrayList<Recipe> getRandomRecipes(ArrayList<Ingredient> excludedIngredients, int[] checkedRecipes) {
        ArrayList<Recipe> recipes;

        recipes = api.getRandomRecipes(checkedRecipes);
        // recipes = GetRandom(excludedIngredients, checkedRecipes);

        return recipes;
    }

    private double getPriceForIngredients(ArrayList<Ingredient> ingredients){
        double price = 0.0;
        for(Ingredient ingredient : ingredients) {
            if(ingredientPrice.containsKey(ingredient.Dutch)){
                price += ingredientPrice.get(ingredient.Dutch);
                continue;
            }
            AHAPIAsync api = new AHAPIAsync(1);
            api.setQuery(ingredient.Dutch);
            api.orderBy(AHAPI.orderBy.ASC);
//            api.setTaxonomy("Groenten");                                      // Set taxonomy

            List<Product> products = null;
            try {
                api.execute();
                products = api.get(10, SECONDS);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            double minPrice = Double.POSITIVE_INFINITY;
            if(products == null){
                Log.d("TEST", "FAILED TO LOAD INGREDIENT PRICE");
                return minPrice;
            }

//            for(Product product : products){          // Since it is only one product and sorted by cheapest, it should be fine.
//                if(product.price < minPrice){
//                    minPrice = product.price;
//                }
//            }

            minPrice = products.get(0).price;
            ingredientPrice.put(ingredient.Dutch, minPrice);
            price+=minPrice;
        }
        return price;
    }
}
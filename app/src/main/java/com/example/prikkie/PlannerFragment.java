package com.example.prikkie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.AHAPIAsync;
import com.example.prikkie.Api.IngredientApi.Product;
import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.ingredientDB.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

public class PlannerFragment extends Fragment {

    private int budget;
    private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    public static final String USER_PREF = "USER_PREF";
    public static final String KEY_BUDGET = "KEY_BUDGET";
    public SharedPreferences sp;

    private ImageView recipePicture;
    private TextView recipeTitle;
    private EditText ingredientList;
    private EditText recipePreperations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sp = (SharedPreferences) getContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        View view =  inflater.inflate(R.layout.fragment_planner,container,false);
        recipePicture = view.findViewById(R.id.generatedRecipePicture);
        ingredientList = view.findViewById(R.id.recipeIngredientList);
        recipePreperations = view.findViewById(R.id.recipePreparations);
        recipeTitle = view.findViewById(R.id.recipeTitle);

        Recipe recipe = getRecipesByBudget();

        String ingredientsListed = "";
        if(recipe != null) {
//        recipePicture.setImageBitmap(recipe.bitmap);
            for (Ingredient ingredient : recipe.ingredients) {
                ingredientsListed += "+ " + ingredient.GetLanguage(1) + "\n";
            }

            recipeTitle.setText(recipe.title);
            ingredientList.setText(ingredientsListed);
            recipePreperations.setText(recipe.method);
        }
        return view;
    }

    public Recipe getRecipesByBudget(){
        if (!sp.contains(KEY_BUDGET)) {
            return null; // budget not found
        }
        budget = sp.getInt(KEY_BUDGET, 0);
        int amountOfRecipes = 4; // get from api (Maybe without the excluded recipes)
        int amountOfCheckedRecipes = 0;
        int[] checkedRecipes = new int[amountOfRecipes];
        Recipe finalRecipe = null;
        ArrayList<Ingredient> excludedIngredients = new ArrayList<Ingredient>();
        // Api get preferences?
        do{
            ArrayList<Recipe> recipes = getRandomRecipes(excludedIngredients, checkedRecipes);
            for(Recipe recipe : recipes){
                Log.d("TEST", "Recipe: "+ recipe.title);
                double recipePrice = getPriceForIngredients(recipe.ingredients);
                if(recipePrice <= budget){
                    finalRecipe = recipe;
                    break;
                }
                if(checkedRecipes.length > 0) {
                    checkedRecipes[amountOfCheckedRecipes] = recipe.id;
                }
                amountOfCheckedRecipes++;
            }
        }while(finalRecipe != null && amountOfCheckedRecipes < amountOfRecipes);

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

    public ArrayList<Recipe> getRandomRecipes(ArrayList<Ingredient> excludedIngredients, int[] checkedRecipes) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        // Api call for recipes with preferences
        // recipes = GetRandom(excludedIngredients, checkedRecipes);

        return getTestRecipes();
    }

    private double getPriceForIngredients(ArrayList<Ingredient> ingredients){
        double price = 0.0;
        for(Ingredient ingredient : ingredients) {
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
                Log.d("TEST", "FAILED TO LOAD");
                return minPrice;
            }
//                    Product cheapest = new Product();

                    for(Product product : products){
                        if(product.price < minPrice){
                            minPrice = product.price;
                        }
                    }

                    price+=minPrice;
        }

        Log.d("TEST", "Total recipe price = " + price);

        return price;
    }
}
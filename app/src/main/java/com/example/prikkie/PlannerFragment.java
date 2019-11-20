package com.example.prikkie;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.ingredientDB.Ingredient;

import java.util.ArrayList;

public class PlannerFragment extends Fragment {

    private int budget;
    private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    public static final String USER_PREF = "USER_PREF";
    public static final String KEY_BUDGET = "KEY_BUDGET";
    public SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sp = (SharedPreferences) getContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return inflater.inflate(R.layout.fragment_planner,container,false);
    }

    public Recipe getRecipesByBudget(){
        if (!sp.contains(KEY_BUDGET)) {
            return null; // budget not found
        }
        budget = sp.getInt(KEY_BUDGET, 0);
        int amountOfRecipes = 0; // get from api (Maybe without the excluded recipes)
        int amountOfCheckedRecipes = 0;
        int[] checkedRecipes = new int[amountOfRecipes];
        Recipe finalRecipe = null;
        ArrayList<Ingredient> excludedIngredients = new ArrayList<Ingredient>();
        // Api get preferences?
        do{
            ArrayList<Recipe> recipes = getRandomRecipes(excludedIngredients, checkedRecipes);
            for(Recipe recipe : recipes){
                double recipePrice = getPriceForIngredients(recipe.ingredients);
                if(recipePrice <= budget){
                    finalRecipe = recipe;
                    break;
                }
                checkedRecipes[amountOfCheckedRecipes] = recipe.id;
                amountOfCheckedRecipes++;
            }
        }while(finalRecipe != null && amountOfCheckedRecipes < amountOfRecipes);

        return finalRecipe;
    }

    public ArrayList<Recipe> getRandomRecipes(ArrayList<Ingredient> excludedIngredients, int[] checkedRecipes) {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();

        // Api call for recipes with preferences
        // recipes = GetRandom(excludedIngredients, checkedRecipes);

        return recipes;
    }

    private double getPriceForIngredients(ArrayList<Ingredient> ingredients){
        double price = 0.0;
        AHAPI api = new AHAPI();

        for(Ingredient ingredient : ingredients){
            price += api.getProducts(new Context(), ingredient.Dutch).get(0).price; // insert context fix
        }

        return price;
    }
}

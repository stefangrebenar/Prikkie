package com.example.prikkie;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.AHAPIAsync;
import com.example.prikkie.Api.IngredientApi.Product;
import com.example.prikkie.Api.recipe_api.PrikkieApi.PrikkieRecipeApi;
import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.RoomShoppingList.ShoppingListItem;
import com.example.prikkie.RoomShoppingList.ShoppingListViewModel;
import com.example.prikkie.ingredientDB.Ingredient;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.example.prikkie.App.hideKeyboardFrom;
import static java.util.concurrent.TimeUnit.SECONDS;

public class HomeDefaultFragment extends Fragment {
    private static HomeDefaultFragment m_fragment;

    public static HomeDefaultFragment getFragment() {
        if (m_fragment == null) {
            m_fragment = new HomeDefaultFragment();
        }
        return m_fragment;
    }

    private HomeDefaultFragment() {
    }

    public EditText budgetText;
    public static final String USER_PREF = "USER_PREF";
    public static final String KEY_BUDGET = "KEY_BUDGET";
    public SharedPreferences sp;
    public Recipe recipe;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private View m_view;
    private ShoppingListViewModel shoppingListViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.fragment_home_default, viewGroup, false);
        shoppingListViewModel = ViewModelProviders.of(getActivity()).get(ShoppingListViewModel.class);

        View addShoppingList = m_view.findViewById(R.id.addToShoppingList);
        addShoppingList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addIngredientsToShoppingList();
            }
        });
        Button search = (Button) m_view.findViewById(R.id.budgetSearchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!budgetText.getText().toString().isEmpty()) {
                    sendBudget();
                    m_view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                    RecipeThread rt = new RecipeThread();
                    Thread t = new Thread(rt);
                    t.start();
                }
                else{
                    Toast msg = Toast.makeText(getContext(), "Vul een budget in", Toast.LENGTH_LONG);
                    msg.show();
                }
            }
        });
        budgetText = (EditText) m_view.findViewById(R.id.budgetID);
        sp = (SharedPreferences) getContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        budgetText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    budgetFilledIn();
                    return true;
                }
                return false;
            }
        });

        if (sp.contains(KEY_BUDGET)) {
            float budget;
            try {
                budget = sp.getFloat(KEY_BUDGET, 0);
            }
            catch(ClassCastException e){
                budget = sp.getInt(KEY_BUDGET, 0);
            }
            budgetText.setText(String.valueOf(budget));
        }

        return m_view;
    }

    public void budgetFilledIn() {
        sendBudget();
    }

    public void sendBudget() {

        float budget = Float.parseFloat(budgetText.getText().toString());
        budget = Float.parseFloat(decimalFormat.format(budget).replace(",", "."));
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(KEY_BUDGET, budget);
        editor.apply();

        hideKeyboardFrom(getContext(), m_view);
    }

    private void addIngredientsToShoppingList(){
        int duration = Toast.LENGTH_SHORT;

        for(Ingredient ingredient : recipe.ingredients) {
            AHAPIAsync api = new AHAPIAsync(1);
            api.setQuery(ingredient.Dutch);
            api.setTaxonomy(ingredient.Taxonomy);
            api.orderBy(AHAPI.orderBy.ASC);
            api.execute();

            try {
                Product product = api.get(1, TimeUnit.SECONDS).get(0);
                ShoppingListItem item = new ShoppingListItem(product.name, product.price, product.imgURL, false);
                shoppingListViewModel.insert(item);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        Toast toast = Toast.makeText(getContext(), "Ingredienten voor " + recipe.title + " zijn toegevoegd aan de boodschappenlijst", duration);
        toast.show();
    }

    // Seperate thread to keep the ui responsive while loading a recipe.
    class RecipeThread implements Runnable {
        private float budget;
        private Recipe cheapestRecipe = null;
        private Map<String, Double> ingredientPrice = new HashMap<String, Double>();
        private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        final PrikkieRecipeApi api = new PrikkieRecipeApi();

        private ImageView recipePicture;
        private TextView recipeDescription;
        private TextView recipeTitle;
        private TextView ingredientList;
        private TextView recipePreperations;

        public RecipeThread() {
        }

        @Override
        public void run() {
            recipe = getRecipesByBudget();
            MainActivity ma = (MainActivity) m_view.getContext();
            if (recipe != null) {
                ma.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String ingredientsListed = "";
                        recipePicture = m_view.findViewById(R.id.generatedRecipePicture);
                        ingredientList = m_view.findViewById(R.id.recipeIngredientList);
                        recipePreperations = m_view.findViewById(R.id.recipePreparations);
                        recipeDescription = m_view.findViewById(R.id.recipe_description);
                        recipeTitle = m_view.findViewById(R.id.recipeTitle);

                        ingredientsListed = recipe.ingredientsToString();

                        Picasso.get().load(recipe.imagePath).resize(recipePicture.getWidth(), recipePicture.getHeight()).into(recipePicture);
                        recipeTitle.setText(recipe.title);
                        recipeDescription.setText(recipe.description);
                        ingredientList.setText(ingredientsListed);
                        recipePreperations.setText(recipe.method);
                        ScrollView sv = m_view.findViewById(R.id.RecipeScrollView);
                        sv.scrollTo(0,0);
                        sv.setVisibility(View.VISIBLE);
                        m_view.findViewById(R.id.recipe_container_layout).setVisibility(View.VISIBLE);
                    }
                });
            }
            else {
                ma.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast msg = Toast.makeText(getContext(), "Er zijn geen recepten gevonden binnen uw budget. Het goedkoopste recept is " + cheapestRecipe.title + " voor €" + cheapestRecipe.price, Toast.LENGTH_LONG);
                        msg.show();
                    }
                });
            }
            ma.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                }
            });
        }

        public Recipe getRecipesByBudget() {
            if (!sp.contains(KEY_BUDGET)) {
                Log.e("Planner fragment", "Budget not found");
                return null; // budget not found
            }
            try {
                budget = sp.getFloat(KEY_BUDGET, 0);
            }
            catch(ClassCastException e){
                budget = sp.getInt(KEY_BUDGET, 0);
            }
            int amountOfRecipes = getAmountOfRecipes(); // get from api (Maybe without the excluded recipes)
            int amountOfCheckedRecipes = 0;
            int[] checkedRecipes = new int[amountOfRecipes];
            Recipe finalRecipe = null;
            ArrayList<Ingredient> excludedIngredients = new ArrayList<Ingredient>();
            // Api get preferences?
            do {
                Log.d("TEST", Arrays.toString(checkedRecipes));
                ArrayList<Recipe> recipes = getRandomRecipes(excludedIngredients, checkedRecipes);
                if (recipes == null) {
                    Log.d("TEST", "didn't get any recipes");
                    return null;
                }
                for (Recipe recipe : recipes) {
                    double recipePrice = getPriceForIngredients(recipe.ingredients);
                    recipe.price = recipePrice;
                    Log.d("TEST", recipe.title + " = " + recipePrice);
                    if (recipePrice <= budget) {
                        finalRecipe = recipe;
                        break;
                    }
                    if(cheapestRecipe == null){
                        cheapestRecipe = recipe;
                    }
                    if(recipe.price < cheapestRecipe.price){
                        cheapestRecipe = recipe;
                    }
                    if (checkedRecipes.length > 0) {
                        checkedRecipes[amountOfCheckedRecipes] = recipe.id;
                    }
                    amountOfCheckedRecipes++;
                }
                if(finalRecipe == null){
                    Log.d("TEST", "FINAL RECIPE == NULL");
                }
            } while (finalRecipe == null && amountOfCheckedRecipes < amountOfRecipes);
            return finalRecipe;
        }

        private int getAmountOfRecipes() {
            return api.getAmountOfRecipes();
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

                AHAPIAsync api = new AHAPIAsync(1);
                api.setQuery(ingredient.Dutch);
                api.orderBy(AHAPI.orderBy.ASC);
                api.setTaxonomy(ingredient.Taxonomy);

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
                if (products == null) {
                    Log.e("Planner fragment", "FAILED TO LOAD");
                    return minPrice;
                }

//            for(Product product : products){          // Since it is only one product and sorted by cheapest, it should be fine.
//                if(product.price < minPrice){
//                    minPrice = product.price;
//                }
//            }

                minPrice = products.get(0).price;
                ingredientPrice.put(ingredient.Dutch, minPrice);
                price += minPrice;
            }
            return price;
        }
    }
}
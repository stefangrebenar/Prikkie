package com.example.prikkie;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.ingredientDB.Ingredient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {
    private static RecipeFragment m_fragment;
    public static RecipeFragment getFragment(){
        if(m_fragment == null){
            m_fragment = new RecipeFragment();
        }
        return m_fragment;
    }
    private RecipeFragment(){}

    private Button searchButton;
    private TextInputEditText searchQuery;
    private TextInputEditText include;
    private TextInputEditText exclude;
    private RecyclerView m_recipeListView;

    private RecipeListAdapter m_adapter;
    private ArrayList<Recipe> recipes;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, viewGroup, false);

        searchButton = view.findViewById(R.id.recipeSubmitButton);
        searchQuery = view.findViewById(R.id.recipeSearch);
        include = view.findViewById(R.id.includedIngredients);
        exclude = view.findViewById(R.id.excludeIngredient);
        m_recipeListView = view.findViewById(R.id.recipeList);

        createExampleList();
        buildRecyclerView();

        Button search = (Button) view.findViewById(R.id.recipeSubmitButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecipe(v);
            }
        });

        return view;
    }

    private void createExampleList(){
        recipes = new ArrayList<Recipe>();

        ArrayList<Ingredient> chocoIngredients = new ArrayList<>();
        chocoIngredients.add(new Ingredient("Melk", "1 liter"));
        chocoIngredients.add(new Ingredient("Chocosticks", "4 stucks"));
        chocoIngredients.add(new Ingredient("Slagroom", "1 spuitbus"));
        chocoIngredients.add(new Ingredient("kruidnoten", "100 gram"));
        chocoIngredients.add(new Ingredient("fudge caramel", "100 gram"));
        Recipe choco = new Recipe("Warme chocolademelk met karamel-zeezoutkruidnoten", "https://ish-images-static.prod.cloud.jumbo.com/product_images/Recipe_502613-01_560x560.jpg", chocoIngredients);

        ArrayList<Ingredient> cheeseIngredients = new ArrayList<>();
        cheeseIngredients.add(new Ingredient("zongedroogde tomaat", "150 gram"));
        cheeseIngredients.add(new Ingredient("sjalot", "1 stuk"));
        cheeseIngredients.add(new Ingredient("augurkenblokjes", "3 eetlepels"));
        cheeseIngredients.add(new Ingredient("beefburgers", "8 stuks"));
        cheeseIngredients.add(new Ingredient("hamburgerbroodjes", "4 stuks"));
        Recipe cheese = new Recipe("Dubbele cheeseburger met tomaat-ui-augurkrelish", "https://static.ah.nl/static/recepten/img_080632_220x162_JPG.jpg", cheeseIngredients);

        ArrayList<Ingredient> pizzaIngredients = new ArrayList<>();
        pizzaIngredients.add(new Ingredient("barbecuesaus", "2 eetlepels"));
        pizzaIngredients.add(new Ingredient("kipdijfiletreepjes", "400 gram"));
        pizzaIngredients.add(new Ingredient("crème fraîse light", "125 gram"));
        pizzaIngredients.add(new Ingredient("gerapste kaas", "125 gram"));
        pizzaIngredients.add(new Ingredient("pizzabodems tomaat", "4 stuks"));
        Recipe pizza = new Recipe("BBQ-chicken pizza", "https://static.ah.nl/static/recepten/img_109519_890x594_JPG.jpg", pizzaIngredients);

        recipes.add(choco);
        recipes.add(cheese);
        recipes.add(pizza);
        recipes.add(choco);
        recipes.add(cheese);
        recipes.add(pizza);
        recipes.add(choco);
        recipes.add(cheese);
        recipes.add(pizza);
    }

    public void showRecipe(View view){
        //Get the recipe keywords
        String keywords = searchQuery.getText().toString();
        //Get the prefered ingredients
        String ingredients = include.getText().toString();
        //Get excluded ingredients
        String excludes = exclude.getText().toString();

//        recipeApi = new RecipePuppy((MainActivity) getActivity(), this);
//        recipeApi.getRecipeFromApi(keywords, ingredients, excludes);
    }

    //Builds the recylerView and sets up the adapter
    public void buildRecyclerView(){
        m_recipeListView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((MainActivity) getContext());
        m_adapter = new RecipeListAdapter(recipes);

        m_recipeListView.setLayoutManager(layoutManager);
        m_recipeListView.setAdapter(m_adapter);

        m_adapter.setOnItemClickListener(new RecipeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Go to recipe page
            }
        });
    }



    public void updateRecipes(ArrayList<Recipe> recipes){
        // Don't continue if there were no recipes found.
        if(recipes == null){
            return;
        }
        // Clear old recipes
        this.recipes.clear();
        // Add all new recipes to list
        this.recipes.addAll(recipes);


//        // If there were any recipes found
//        if(recipes.size() > 0) {
//            Recipe recipe = recipes.get(0);
//            // Show recipe
//            recipeView.setText(recipe.title + "\n" + recipe.href + "\n\n");
//            for(int i = 0; i < recipe.ingredients.size(); i++){
//                recipeView.append(recipe.ingredients.get(i)+ ", ");
//            }
//            if (!recipe.imagePath.isEmpty()) //An image exists
//            {
//                Picasso.get().load(recipe.imagePath).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(imageView);
//            }
//        }
    }
}
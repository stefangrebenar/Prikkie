package com.example.prikkie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prikkie.Api.recipe_api.PrikkieApi.PrikkieRecipeApi;
import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.ingredientDB.Ingredient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

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

        buildRecyclerView();
        showRecipe(view);

        Button search = (Button) view.findViewById(R.id.recipeSubmitButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecipe(v);
            }
        });

        return view;
    }

    public void showRecipe(View view){
        //Get the recipe keywords
        String keywords = searchQuery.getText().toString();
        //Get the prefered ingredients
        String ingredients = include.getText().toString();
        //Get excluded ingredients
        String excludes = exclude.getText().toString();

        RecipeThread thread = new RecipeThread();
        thread.name = keywords;
        thread.includes = ingredients;
        thread.excludes = excludes;
        thread.run();
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

    class RecipeThread implements Runnable{
        public String name;
        public String includes;
        public String excludes;
        public int page;

        private ArrayList<Recipe> recipes;
        private PrikkieRecipeApi api = new PrikkieRecipeApi();


        @Override
        public void run() {
            recipes = getRecipesFromApi();
            if(recipes == null){
                return;
            }

            m_adapter.setRecipes(recipes);
        }

        private ArrayList<Recipe> getRecipesFromApi(){
            ArrayList<Recipe> recipes = new ArrayList<Recipe>();
            recipes = api.getRecipeFromApi(name, includes, excludes, page);


            return recipes;
        }
    }
}
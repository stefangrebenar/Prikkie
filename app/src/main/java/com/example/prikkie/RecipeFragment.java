package com.example.prikkie;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.Api.recipe_api.RecipeApi;
import com.example.prikkie.Api.recipe_api.RecipePuppy;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeFragment extends Fragment {

    private TextView recipeView;
    private Button searchButton;
    private ImageView imageView;
    private TextInputEditText searchQuery;
    private TextInputEditText include;
    private TextInputEditText exclude;
    private RecipeApi recipeApi;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, viewGroup, false);

        recipeView = view.findViewById(R.id.recipeView);
        searchButton = view.findViewById(R.id.recipeSubmitButton);
        searchQuery = view.findViewById(R.id.recipeSearch);
        imageView = view.findViewById(R.id.imageView);
        include = view.findViewById(R.id.includedIngredients);
        exclude = view.findViewById(R.id.excludeIngredient);


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

        recipeApi = new RecipePuppy(((MainActivity) getActivity()));
        recipeApi.getRecipeFromApi(keywords, ingredients, excludes);
    }

    public void updateRecipes(ArrayList<Recipe> recipes){

        // If there were any recipes found
        if(recipes.size() > 0) {
            Recipe recipe = recipes.get(0);
            // Show recipe
            recipeView.setText(recipe.title + "\n" + recipe.href + "\n\n");
            for(int i = 0; i < recipe.ingredients.size(); i++){
                recipeView.append(recipe.ingredients.get(i)+ ", ");
            }
            if (!recipe.imagePath.isEmpty()) //An image exists
            {
                Picasso.get().load(recipe.imagePath).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(imageView);
            }
        }
    }
}
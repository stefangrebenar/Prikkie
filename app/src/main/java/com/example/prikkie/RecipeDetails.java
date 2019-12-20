package com.example.prikkie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prikkie.Api.recipe_api.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class RecipeDetails extends Fragment {
    private static RecipeDetails m_fragment;
    public static RecipeDetails getFragment(){
        if(m_fragment == null){
            m_fragment = new RecipeDetails();
        }
        return m_fragment;
    }
    private RecipeDetails(){}

    private static Recipe recipe;
    private static boolean newRecipe = true;
    private ImageView image;
    private static ScrollView sv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details_fragment, viewGroup, false);
        ImageView back = (ImageView) view.findViewById(R.id.back_button);
        if(back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFragment(RecipeFragment.getFragment());
                }
            });
        }
        if(recipe != null){
            image = (ImageView) view.findViewById(R.id.generatedRecipePicture);
            TextView title = (TextView) view.findViewById(R.id.recipeTitle);
            TextView description = (TextView) view.findViewById(R.id.recipe_description);
            TextView ingredients = (TextView) view.findViewById(R.id.recipeIngredientList);
            TextView method = (TextView) view.findViewById(R.id.recipePreparations);

            RequestCreator recipeImage = Picasso.get().load(recipe.imagePath);//.into(image);//.resize(image.getWidth(), image.getHeight())
            recipeImage.resize(256, 128).centerInside();
            recipeImage.into(image);
            title.setText(recipe.title);
            description.setText(recipe.description);
            ingredients.setText(recipe.ingredientsToString());
            method.setText(recipe.method);
        }
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(newRecipe) {
            sv = this.getView().findViewById(R.id.RecipeScrollView);
            sv.post(new Runnable() {
                @Override
                public void run() {
                    sv.scrollTo(0, 0);
                }
            });
            sv.setVisibility(View.VISIBLE);
        }
    }

    public static void setRecipe(Recipe recipe){
        if(RecipeDetails.recipe != null) {
            if (RecipeDetails.recipe.ingredientsToString().equals(recipe.ingredientsToString())) {
                newRecipe = false;
                return;
            }
        }
        RecipeDetails.recipe = recipe;
        newRecipe = true;
        if(sv != null) {
            sv.setVisibility(View.INVISIBLE);
        }
    }

    public void setFragment(Fragment fragment) {
        MainActivity ma = (MainActivity)getActivity();
        ma.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.frame_container, fragment).commit();
    }
}

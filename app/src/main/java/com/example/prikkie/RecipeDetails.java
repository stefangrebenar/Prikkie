package com.example.prikkie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.prikkie.Api.IngredientApi.AHAPI;
import com.example.prikkie.Api.IngredientApi.AHAPIAsync;
import com.example.prikkie.Api.IngredientApi.Product;
import com.example.prikkie.Api.recipe_api.Recipe;
import com.example.prikkie.RoomShoppingList.ShoppingListItem;
import com.example.prikkie.RoomShoppingList.ShoppingListViewModel;
import com.example.prikkie.ingredientDB.Ingredient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.example.prikkie.RecipeDetails.Fragment.*;

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
    private View m_view;
    private ImageView image;
    private static ScrollView sv;
    private ShoppingListViewModel shoppingListViewModel;
    public static enum Fragment {
        RECIPES,
        WEEKLYPLANNER
    }
    public Fragment PreviousFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details_fragment, viewGroup, false);
        m_view = view;
        shoppingListViewModel = ViewModelProviders.of(getActivity()).get(ShoppingListViewModel.class);
        ImageView back = (ImageView) view.findViewById(R.id.back_button);
        if(back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(PreviousFragment == RECIPES) {
                        setFragment(RecipeFragment.getFragment());
                    }
                    else if(PreviousFragment == WEEKLYPLANNER){
                            setFragmentMainActivity(WeeklyPlannerFragment.getFragment());
                    }
                }
            });
        }
        View addShoppingList = view.findViewById(R.id.addToShoppingList);
        addShoppingList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addIngredientsToShoppingList();
            }
        });
        if(recipe != null){
            image = (ImageView) view.findViewById(R.id.generatedRecipePicture);
            TextView title = (TextView) view.findViewById(R.id.recipeTitle);
            TextView description = (TextView) view.findViewById(R.id.recipe_description);
            TextView ingredients = (TextView) view.findViewById(R.id.recipeIngredientList);
            TextView method = (TextView) view.findViewById(R.id.recipePreparations);

            RequestCreator recipeImage = Picasso.get().load(recipe.imagePath);
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

    private void addIngredientsToShoppingList() {

        Runnable shoppingThread = new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m_view.findViewById(R.id.progressBarRecipeDetails).setVisibility(View.VISIBLE);
                    }
                });
                for (Ingredient ingredient : recipe.ingredients) {
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m_view.findViewById(R.id.progressBarRecipeDetails).setVisibility(View.INVISIBLE);
                        Toast toast = Toast.makeText(getContext(), "Ingredienten voor " + recipe.title + " zijn toegevoegd aan de boodschappenlijst", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        };
        Thread t = new Thread(shoppingThread);
        t.start();
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

    public void setFragment(androidx.fragment.app.Fragment fragment) {
        MainActivity ma = (MainActivity)getActivity();
        ma.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.frame_container, fragment).commit();
    }

    public void setFragmentMainActivity(androidx.fragment.app.Fragment fragment) {
        MainActivity ma = (MainActivity)getActivity();
        ma.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container, fragment).commit();
    }
}

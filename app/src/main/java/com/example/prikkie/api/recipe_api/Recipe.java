package com.example.prikkie.Api.recipe_api;
import android.graphics.Bitmap;

import com.example.prikkie.ingredientDB.Ingredient;

import java.util.ArrayList;

public class Recipe {
    public int id;
    public String title;
    public String imagePath; // will probably be deprecated
    public ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    public String description;
    public String method;
    public int persons;
    public double price;

    public Recipe(){
    }

    public Recipe(String title, String imagePath, ArrayList<Ingredient> ingredients, String method){
        this.title = title;
        this.imagePath = imagePath;
        this.ingredients = ingredients;
        this.method = method;
    }

    public Recipe(String title, String imagePath, ArrayList<Ingredient> ingredients){
        this.title = title;
        this.imagePath = imagePath;
        this.ingredients = ingredients;
    }

    public String ingredientsToString(){
        String result = "";

        for(Ingredient ingredient : ingredients){
            result += "- " + ingredient.Dutch + " " + ingredient.amount + " " + ingredient.unit + "\n";
        }

        return result;
    }
}

package com.example.prikkie.Api.recipe_api;
import android.graphics.Bitmap;

import com.example.prikkie.ingredientDB.Ingredient;

import java.util.ArrayList;

public class Recipe {
    public int id;
    public String title;
    public String imagePath; // will probably be deprecated
    public Bitmap bitmap; // probably the type of image
    public ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
    public String discription;
    public String method;
    public int persons;
    public String href; // will be deprecated
}

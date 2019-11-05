package com.example.prikkie.ingredients.presets;

import com.example.prikkie.ingredients.IngredientClassificationComponent;
import com.example.prikkie.ingredients.animals.Animals;

public class Vegetarian extends IngredientClassificationComponent {
    public Vegetarian(){
        English = "Vegetarian";
        Dutch = "Vegetarisch";

        Add(new Animals());
    }
}

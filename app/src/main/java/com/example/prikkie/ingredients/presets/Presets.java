package com.example.prikkie.ingredients.presets;

import com.example.prikkie.ingredients.IngredientClassificationComponent;
import com.example.prikkie.ingredients.presets.Vegan;
import com.example.prikkie.ingredients.presets.Vegetarian;

public class Presets extends IngredientClassificationComponent {
    public Presets(){
        English = "Presets";
        Dutch = "Vooraf ingesteld";

        Add(new Vegan());
        Add(new Vegetarian());
    }
}

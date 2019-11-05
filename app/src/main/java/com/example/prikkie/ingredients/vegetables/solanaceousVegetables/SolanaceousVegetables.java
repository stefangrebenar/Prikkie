package com.example.prikkie.ingredients.vegetables.solanaceousVegetables;

import com.example.prikkie.ingredients.IngredientClassificationComponent;
import com.example.prikkie.ingredients.vegetables.potatoes.Potato;

public class SolanaceousVegetables extends IngredientClassificationComponent {
    public SolanaceousVegetables(){
        English = "SolanaceousVegetables";
        Dutch = "Nachtschades";

        Add(new EggPlant());
        Add(new Potato());
        Add(new SweetPepper());
        Add(new Tomato());
    }
}

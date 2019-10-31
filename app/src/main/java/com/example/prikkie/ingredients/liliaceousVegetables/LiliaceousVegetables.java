package com.example.prikkie.ingredients.liliaceousVegetables;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class LiliaceousVegetables extends IngredientClassificationComponent {
    public LiliaceousVegetables(){
        English = "Liliaceous vegetables";
        Dutch = "Leliegroenten ";

        Add(new Asparagus());
        Add(new Garlic());
        Add(new Onion());
        Add(new Welsh());
    }
}

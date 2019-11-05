package com.example.prikkie.ingredients.vegetables.compositeVegetables;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class CompositeVegetables extends IngredientClassificationComponent {
    public CompositeVegetables(){
        English = "Composite vegetables";
        Dutch = "Composite groenten";

        Add(new Artichoke());
        Add(new Burdock());
        Add(new Chicory());
        Add(new Endive());
        Add(new Lettuce());
        Add(new Salsify());
    }
}

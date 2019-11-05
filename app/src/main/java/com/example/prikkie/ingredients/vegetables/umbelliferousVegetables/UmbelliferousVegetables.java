package com.example.prikkie.ingredients.vegetables.umbelliferousVegetables;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class UmbelliferousVegetables extends IngredientClassificationComponent {
    public UmbelliferousVegetables(){
        English = "Umbelliferous vegetables";
        Dutch = "Schermbloemige groenten";

        Add(new Carrot());
        Add(new Celery());
        Add(new Parsley());
        Add(new Parsnip());
    }
}

package com.example.prikkie.ingredients.vegetables.cerealGrains;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class CerealGrains extends IngredientClassificationComponent {

    public CerealGrains(){
        English = "Cereal Grains";
        Dutch = "Granen";

        Add(new Rice());
        Add(new Wheat());
        Add(new Barley());
        Add(new Rye());
        Add(new Corn());
        Add(new Buckwheat());
    }
}

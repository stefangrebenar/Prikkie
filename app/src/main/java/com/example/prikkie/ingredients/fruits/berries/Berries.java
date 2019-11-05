package com.example.prikkie.ingredients.fruits.berries;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class Berries extends IngredientClassificationComponent {
    public Berries(){
        English = "Berries";
        Dutch = "Bessen";

        Add(new Strawberry());
        Add(new Raspberry());
        Add(new Blackberry());
        Add(new Blueberry());
        Add(new Cranberry());
        Add(new Huckleberry());
    }
}

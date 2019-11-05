package com.example.prikkie.ingredients.animals.meat;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class Meat extends IngredientClassificationComponent {
    public Meat(){
        English = "Meat";
        Dutch = "Vlees";

        Add(new Beef());
        Add(new Cow());
        Add(new Deer());
        Add(new Goat());
        Add(new Horse());
        Add(new Pig());
        Add(new Pork());
        Add(new Sheep());
    }
}

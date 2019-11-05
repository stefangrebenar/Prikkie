package com.example.prikkie.ingredients.animals.poultry;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class Poultry extends IngredientClassificationComponent {
    public Poultry(){
        English = "Poultry";
        Dutch = "Gevogelte";

        Add(new Chicken());
        Add(new Duck());
        Add(new Turkey());
    }
}

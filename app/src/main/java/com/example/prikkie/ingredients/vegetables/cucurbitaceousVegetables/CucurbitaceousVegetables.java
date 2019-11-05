package com.example.prikkie.ingredients.vegetables.cucurbitaceousVegetables;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class CucurbitaceousVegetables extends IngredientClassificationComponent {
    public CucurbitaceousVegetables(){
        English = "Cucurbitaceous vegetables";
        Dutch = "Komkommerachtige groenten";

        Add(new Cucumber());
        Add(new Melon());
        Add(new Pumpkin());
        Add(new WaterMelon());
    }
}

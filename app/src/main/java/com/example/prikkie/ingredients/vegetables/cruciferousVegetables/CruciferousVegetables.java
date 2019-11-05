package com.example.prikkie.ingredients.vegetables.cruciferousVegetables;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class CruciferousVegetables extends IngredientClassificationComponent {
    public CruciferousVegetables(){
        English = "Cruciferous vegetables";
        Dutch = "Kruisbloemige groenten";

        Add(new Broccoli());
        Add(new BrusselsSprouts());
        Add(new Cabbage());
        Add(new Cauliflower());
        Add(new ChineseCabbage());
        Add(new Horseradish());
        Add(new Kale());
        Add(new Komatsuna());
        Add(new Kyona());
        Add(new Radish());
        Add(new Turnip());
        Add(new Watercress());
    }
}

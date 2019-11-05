package com.example.prikkie.ingredients.animals.aquaticAnimals.fish;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class Fish extends IngredientClassificationComponent {
    public Fish(){
        English = "Fish";
        Dutch = "Vis";

        Add(new Eel());
        Add(new Mackerel());
        Add(new Salmon());
        Add(new SeaBass());
        Add(new SeaBream());
        Add(new Trout());
        Add(new Tuna());
    }
}

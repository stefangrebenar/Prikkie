package com.example.prikkie.ingredients.animals;

import com.example.prikkie.ingredients.IngredientClassificationComponent;
import com.example.prikkie.ingredients.animals.aquaticAnimals.AquaticAnimals;
import com.example.prikkie.ingredients.animals.meat.Meat;
import com.example.prikkie.ingredients.animals.poultry.Poultry;

public class Animals extends IngredientClassificationComponent {
    public Animals(){
        English = "Animals";
        Dutch = "Dieren";

        Add(new AquaticAnimals());
        Add(new Meat());
        Add(new Poultry());
    }
}

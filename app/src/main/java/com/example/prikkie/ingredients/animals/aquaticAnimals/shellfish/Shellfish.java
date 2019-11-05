package com.example.prikkie.ingredients.animals.aquaticAnimals.shellfish;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class Shellfish extends IngredientClassificationComponent {
    public Shellfish(){
        English = "Shellfish";
        Dutch = "Schaaldieren";

        Add(new Cockles());
        Add(new Crab());
        Add(new Gamba());
        Add(new Lobster());
        Add(new Mussel());
        Add(new Oyster());
        Add(new Shrimp());
        Add(new Squid());
    }
}

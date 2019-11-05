package com.example.prikkie.ingredients.animals.aquaticAnimals;

import com.example.prikkie.ingredients.IngredientClassificationComponent;
import com.example.prikkie.ingredients.animals.aquaticAnimals.fish.Fish;
import com.example.prikkie.ingredients.animals.aquaticAnimals.shellfish.Shellfish;

public class AquaticAnimals extends IngredientClassificationComponent {
    public AquaticAnimals(){
        English = "Aquatic animals";
        Dutch = "Waterdieren";

        Add(new Fish());
        Add(new Shellfish());
    }
}

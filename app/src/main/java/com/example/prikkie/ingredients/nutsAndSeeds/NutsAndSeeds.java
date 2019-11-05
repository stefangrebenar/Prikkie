package com.example.prikkie.ingredients.nutsAndSeeds;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class NutsAndSeeds extends IngredientClassificationComponent {
    public NutsAndSeeds(){
        English = "Nuts and seeds";
        Dutch = "Noten en zaden";

        Add(new Almond());
        Add(new Chestnut());
        Add(new CottonSeeds());
        Add(new Peanut());
        Add(new Pecan());
        Add(new Rapesseeds());
        Add(new SafflowerSeeds());
        Add(new SesamSeeds());
        Add(new SunflowerSeeds());
        Add(new Walnut());
    }
}

package com.example.prikkie.ingredients.fruits.citrusFruits;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class CitrusFruits extends IngredientClassificationComponent {
    public CitrusFruits(){
        English = "Citrus fruits";
        Dutch = "Citrus vruchten";

        Add(new Citrus());
        Add(new Lemon());
        Add(new Orange());
        Add(new Grapefruit());
        Add(new Lime());
    }
}

package com.example.prikkie.ingredients.fruits.pomeFruits;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class PomeFruits extends IngredientClassificationComponent {
    public PomeFruits(){
        English = "Pome fruits";
        Dutch = "Pitvruchten";

        Add(new Apple());
        Add(new JapanesePear());
        Add(new Pear());
        Add(new Quince());
        Add(new Loquat());
    }
}

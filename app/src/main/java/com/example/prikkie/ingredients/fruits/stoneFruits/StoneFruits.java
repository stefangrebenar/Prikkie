package com.example.prikkie.ingredients.fruits.stoneFruits;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class StoneFruits extends IngredientClassificationComponent {
    public StoneFruits(){
        English = "Stone fruits";
        Dutch = "Steenfruit";

        Add(new Peach());
        Add(new Nectarine());
        Add(new Apricot());
        Add(new JapanesePlume());
        Add(new Prune());
        Add(new MumePlum());
        Add(new Cherry());
    }
}

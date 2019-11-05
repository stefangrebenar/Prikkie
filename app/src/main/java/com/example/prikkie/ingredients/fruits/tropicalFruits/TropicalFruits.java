package com.example.prikkie.ingredients.fruits.tropicalFruits;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class TropicalFruits extends IngredientClassificationComponent {
    public TropicalFruits(){
        English = "Tropical fruits";
        Dutch = "Tropische vruchten";

        Add(new Avocado());
        Add(new Banana());
        Add(new Guava());
        Add(new JapanesePersimmon());
        Add(new Kiwi());
        Add(new Mango());
        Add(new Papaya());
        Add(new PassionFruit());
        Add(new Pineapple());
    }
}

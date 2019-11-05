package com.example.prikkie.ingredients.fruits;

import com.example.prikkie.ingredients.fruits.berries.Berries;
import com.example.prikkie.ingredients.fruits.citrusFruits.CitrusFruits;
import com.example.prikkie.ingredients.fruits.pomeFruits.PomeFruits;
import com.example.prikkie.ingredients.fruits.stoneFruits.StoneFruits;
import com.example.prikkie.ingredients.fruits.tropicalFruits.TropicalFruits;
import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class Fruits extends IngredientClassificationComponent {
    public Fruits(){
        English = "Fruits";
        Dutch = "Vegetables";

        Add(new Berries());
        Add(new CitrusFruits());
        Add(new PomeFruits());
        Add(new StoneFruits());
        Add(new TropicalFruits());
        Add(new Grape());
    }
}

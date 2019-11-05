package com.example.prikkie.ingredients;

import com.example.prikkie.ingredients.animalProducts.AnimalProducts;
import com.example.prikkie.ingredients.animals.Animals;
import com.example.prikkie.ingredients.fruits.Fruits;
import com.example.prikkie.ingredients.nutsAndSeeds.NutsAndSeeds;
import com.example.prikkie.ingredients.vegetables.Vegetables;

public class Ingredients extends IngredientClassificationComponent {
    public Ingredients(){
        English = "Ingredients";
        Dutch = "Ingrediënten";

        Add(new AnimalProducts());
        Add(new Animals());
        Add(new Fruits());
        Add(new NutsAndSeeds());
        Add(new Vegetables());
    }
}

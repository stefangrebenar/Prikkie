package com.example.prikkie.ingredients.presets;

import com.example.prikkie.ingredients.IngredientClassificationComponent;
import com.example.prikkie.ingredients.animalProducts.AnimalProducts;
import com.example.prikkie.ingredients.animals.Animals;

public class Vegan extends IngredientClassificationComponent {
    public Vegan(){
        English = "Vegan";
        Dutch = "Veganistisch";

        Add(new Animals());
        Add(new AnimalProducts());
    }
}

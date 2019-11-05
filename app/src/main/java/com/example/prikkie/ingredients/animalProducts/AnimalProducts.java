package com.example.prikkie.ingredients.animalProducts;

import com.example.prikkie.ingredients.IngredientClassificationComponent;
import com.example.prikkie.ingredients.animalProducts.dairyProducts.DairyProducts;

public class AnimalProducts extends IngredientClassificationComponent {
    public AnimalProducts(){
        English = "Animal products";
        Dutch = "Dierlijke producten";

        Add(new Eggs());
        Add(new Honey());
        Add(new DairyProducts());
    }
}

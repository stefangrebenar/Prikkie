package com.example.prikkie.ingredients.animalProducts.dairyProducts;

import com.example.prikkie.ingredients.IngredientClassificationComponent;

public class DairyProducts extends IngredientClassificationComponent {
    public DairyProducts(){
        English = "Dairy products";
        Dutch = "Zuivelproducten";

        Add(new Butter());
        Add(new Buttermilk());
        Add(new Cheese());
        Add(new CottageCheese());
        Add(new Cream());
        Add(new Custard());
        Add(new Milk());
        Add(new SourCream());
        Add(new Yogurt());
    }
}

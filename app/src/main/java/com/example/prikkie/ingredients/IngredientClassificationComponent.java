package com.example.prikkie.ingredients;

import java.util.ArrayList;

public abstract class IngredientClassificationComponent {
    public String English;
    public String Dutch;
    public ArrayList<IngredientClassificationComponent> SubIngredients = new ArrayList<IngredientClassificationComponent>();

    public void Add(IngredientClassificationComponent ingredient){
        if(!SubIngredients.contains(ingredient)){   // if the ingredient hasn't been add already
            SubIngredients.add(ingredient);
        }
    }

    public void Remove(IngredientClassificationComponent ingredient){
        if(SubIngredients.contains(ingredient)){    // if it does contain the ingredient
            SubIngredients.remove(ingredient);      // not sure if the if-statement is nessecairy
        }
    }

    public String GetEnglish(){
        return  English;
    }

    public String GetDutch(){
        return Dutch;
    }

    public ArrayList<IngredientClassificationComponent> GetComponents(){
        return SubIngredients;
    }
}

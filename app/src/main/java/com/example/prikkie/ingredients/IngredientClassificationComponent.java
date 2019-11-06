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

    public String GetLanguage(int i){
       switch(i) {
           case 0:
               return English;
           case 1:
               return Dutch;
       }
       return "Language not found";
    }

    public ArrayList<IngredientClassificationComponent> GetComponents(){
        return SubIngredients;
    }

    public ArrayList<IngredientClassificationComponent> GetAllComponents()
    {
        ArrayList<IngredientClassificationComponent> allComponents = new ArrayList<IngredientClassificationComponent>();
        for(IngredientClassificationComponent component: SubIngredients){
            allComponents.add(component);
            allComponents.addAll(component.GetAllComponents());
        }
        return allComponents;
    }
}

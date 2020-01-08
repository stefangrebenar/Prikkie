package com.example.prikkie.RoomShoppingList;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {
    private ShoppingListRepository repository;
    private LiveData<List<ShoppingListItem>> allItems;

    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        repository = new ShoppingListRepository(application);
        allItems = repository.getAllItems();
    }

    public void insert(ShoppingListItem shoppingListItem){
        repository.insert(shoppingListItem);
    }

    public void update(ShoppingListItem shoppingListItem){
        repository.update(shoppingListItem);
    }

    public void delete(ShoppingListItem shoppingListItem){
        repository.delete(shoppingListItem);
    }

    public void deleteAll(){
        repository.deleteAllItems();
    }

    public LiveData<List<ShoppingListItem>> getAllItems() {
        return allItems;
    }
}

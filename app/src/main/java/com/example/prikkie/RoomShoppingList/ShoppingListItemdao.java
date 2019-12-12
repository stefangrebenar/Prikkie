package com.example.prikkie.RoomShoppingList;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingListItemdao {

    @Insert
    void insert(ShoppingListItem shoppingListItem);

    @Update
    void update(ShoppingListItem shoppingListItem);

    @Delete
    void delete(ShoppingListItem shoppingListItem);

    @Query("DELETE FROM shoppinglistitem_table")
    void deleteAll();

    @Query("SELECT * FROM shoppinglistitem_table")
    LiveData<List<ShoppingListItem>> getAllShoppingListItems();
}

package com.example.prikkie.RoomShoppingList;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingListRepository {
    private ShoppingListItemdao shoppingListItemdao;
    private LiveData<List<ShoppingListItem>> allItems;

    public ShoppingListRepository(Application application){
        ShoppingListDatabase database = ShoppingListDatabase.getInstance(application);
        shoppingListItemdao = database.shoppingListItemdao();
        allItems =  shoppingListItemdao.getAllShoppingListItems();
    }

    public void insert(ShoppingListItem shoppingListItem){
        new InsertItemAsyncTask(shoppingListItemdao).execute(shoppingListItem);
    }
    public void update(ShoppingListItem shoppingListItem){
        new UpdateItemAsyncTask(shoppingListItemdao).execute(shoppingListItem);
    }
    public void delete(ShoppingListItem shoppingListItem){
        new DeleteItemAsyncTask(shoppingListItemdao).execute(shoppingListItem);
    }
    public void deleteAllItems(){
        new DeleteAllItemsAsync(shoppingListItemdao).execute();
    }

    public LiveData<List<ShoppingListItem>> getAllItems(){
        return allItems;
    }

    private static class InsertItemAsyncTask extends AsyncTask<ShoppingListItem, Void, Void>{
        private ShoppingListItemdao shoppingListItemdao;

        private InsertItemAsyncTask(ShoppingListItemdao shoppingListItemdao){
            this.shoppingListItemdao = shoppingListItemdao;
        }

        @Override
        protected Void doInBackground(ShoppingListItem... shoppingListItems) {
            shoppingListItemdao.insert(shoppingListItems[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<ShoppingListItem, Void, Void>{
        private ShoppingListItemdao shoppingListItemdao;

        private UpdateItemAsyncTask(ShoppingListItemdao shoppingListItemdao){
            this.shoppingListItemdao = shoppingListItemdao;
        }

        @Override
        protected Void doInBackground(ShoppingListItem... shoppingListItems) {
            shoppingListItemdao.update(shoppingListItems[0]);
            return null;
        }
    }
    private static class DeleteItemAsyncTask extends AsyncTask<ShoppingListItem, Void, Void>{
        private ShoppingListItemdao shoppingListItemdao;

        private DeleteItemAsyncTask(ShoppingListItemdao shoppingListItemdao){
            this.shoppingListItemdao = shoppingListItemdao;
        }

        @Override
        protected Void doInBackground(ShoppingListItem... shoppingListItems) {
            shoppingListItemdao.delete(shoppingListItems[0]);
            return null;
        }
    }
    private static class DeleteAllItemsAsync extends AsyncTask<Void, Void, Void>{
        private ShoppingListItemdao shoppingListItemdao;

        private DeleteAllItemsAsync(ShoppingListItemdao shoppingListItemdao){
            this.shoppingListItemdao = shoppingListItemdao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            shoppingListItemdao.deleteAll();
            return null;
        }
    }
}


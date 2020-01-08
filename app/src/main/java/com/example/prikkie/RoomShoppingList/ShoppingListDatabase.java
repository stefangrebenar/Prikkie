package com.example.prikkie.RoomShoppingList;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ShoppingListItem.class}, version = 2)
public abstract class ShoppingListDatabase extends RoomDatabase {
    private static ShoppingListDatabase instance;

    public abstract ShoppingListItemdao shoppingListItemdao();

    public static synchronized ShoppingListDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ShoppingListDatabase.class, "shoppingList_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private ShoppingListItemdao shoppingListItemdao;

        private PopulateDbAsyncTask(ShoppingListDatabase db){
            shoppingListItemdao = db.shoppingListItemdao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            shoppingListItemdao.insert(new ShoppingListItem("Kaas", 5.95, "iets", false));

            return null;
        }
    }
}

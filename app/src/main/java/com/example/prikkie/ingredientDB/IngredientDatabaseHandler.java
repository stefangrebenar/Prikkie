package com.example.prikkie.ingredientDB;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class IngredientDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "IngredientDB";
    // INGREDIENTS
    private static final String TABLE_NAME_INGREDIENTS = "ingredients";
    private static final String KEY_ID_INGREDIENTS = "id";
    private static final String KEY_ENGLISH = "english";
    private static final String KEY_DUTCH = "dutch";
    private static final String KEY_CHECKED = "checked";
    private static final String[] COLUMNS_INGREDIENTS = {KEY_ID_INGREDIENTS, KEY_ENGLISH, KEY_DUTCH, KEY_CHECKED};
    // INGREDIENT COUPLES

    // MIGRATIONS
    private static final String TABLE_NAME_MIGRATIONS = "migrations";
    private static final String KEY_ID_MIGRATIONS = "id";
    private static final String[] COLUMNS_MIGRATIONS = {KEY_ID_MIGRATIONS};
    private Context context;


    public IngredientDatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.onCreate(this.getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create ingredient table
        final String CREATE_INGREDIENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_INGREDIENTS + " (" + KEY_ID_INGREDIENTS + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ENGLISH + " TEXT, " + KEY_DUTCH + " TEXT, " + KEY_CHECKED + " BIT)";
        // Create ingredient couple table

        // Create migration table
        final String CREATE_MIGRATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MIGRATIONS + " (" + KEY_ID_MIGRATIONS + " INTEGER PRIMARY KEY AUTOINCREMENT, value INT)";

        db.execSQL(CREATE_INGREDIENT_TABLE);
        db.execSQL(CREATE_MIGRATION_TABLE);

        // Read migrations
        try {
            // to reach asset
            AssetManager assetManager = context.getAssets();
            // to get all item in dogs folder.
            String[] migrations = assetManager.list("migrations");
            InputStream inputStream;
            for (int i = 0; i < migrations.length; i++) {
                inputStream = context.getAssets().open("migrations/migration" + (i+1) + ".sql");

                    int lineNumber = 0;
                    InputStreamReader fr = new InputStreamReader(inputStream);   //reads the file
                    BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
                    String line = br.readLine();
                    if (line != null) {
                        if (lineNumber == 0) {
                            lineNumber++;
                            // Check if db contains id of migration
                            if (doesMigrationExist(Integer.parseInt(line))) {
                                break;
                            }
                            // If not already executed, add it to db
                            db.execSQL("INSERT INTO " + TABLE_NAME_MIGRATIONS + " (value) VALUES (0)");
                        }
                    } else {
                        break;
                    }
                    while ((line = br.readLine()) != null) {
                        if (!line.trim().isEmpty())
                            db.execSQL(line);
                    }
                    fr.close();    //closes the stream and release the resources
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public boolean doesMigrationExist(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MIGRATIONS,
                COLUMNS_MIGRATIONS,
                KEY_ID_MIGRATIONS + " = ?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null,
                null
        );
        if(cursor != null)
            cursor.moveToFirst();
        return (cursor.getCount() > 0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INGREDIENTS);
        this.onCreate(db);
    }

    public void deleteOne(Ingredient ingredient){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_INGREDIENTS, KEY_ID_INGREDIENTS + " = ?", new String[] {String.valueOf(ingredient.Id)});
        db.close();
    }

    public Ingredient getIngredient(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_INGREDIENTS,
                COLUMNS_INGREDIENTS,
                KEY_ID_INGREDIENTS + " = ?", // selection
                new String[] {String.valueOf(id)}, // params
                null,
                null,
                null,
                null
        );

        if(cursor != null){
            cursor.moveToFirst();
        }
        if(cursor.getCount() > 0) {
            boolean checked = (Integer.parseInt(cursor.getString(3)) == 1);
            Ingredient ingredient = new Ingredient(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    checked);

            return ingredient;
        }
        return null;
    }

    public ArrayList<Ingredient> AllIngredients(){
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        String query = "SELECT * FROM " + TABLE_NAME_INGREDIENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Ingredient ingredient = null;

        if(cursor.moveToFirst()){
            do {

                boolean checked = (Integer.parseInt(cursor.getString(3)) == 1);
                ingredient = new Ingredient(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        checked);

                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }

        return ingredients;
    }

    public void AddIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ENGLISH, ingredient.English);
        values.put(KEY_DUTCH, ingredient.Dutch);

        db.insert(TABLE_NAME_INGREDIENTS, null, values);
        db.close();
    }

    public int updateIngredient(Ingredient ingredient){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("checked",ingredient.Checked);
        int i = db.update(TABLE_NAME_INGREDIENTS,
                values,
                KEY_ID_INGREDIENTS + " = ?",
                new String[] {String.valueOf(ingredient.Id)});

        db.close();
        Log.d("TEST", "Updated database");
        return i;
    }

    public ArrayList<Ingredient> GetCheckedIngredients(){
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        String query = "SELECT * FROM " + TABLE_NAME_INGREDIENTS + " WHERE checked == 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Ingredient ingredient = null;

        if(cursor.moveToFirst()){
            do {

                boolean checked = (Integer.parseInt(cursor.getString(3)) == 1);
                ingredient = new Ingredient(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        checked);

                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }

        return ingredients;
    }
}

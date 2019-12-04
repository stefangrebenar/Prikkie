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
    private static final String TABLE_NAME_COUPLE = "ingredientCouples";
    private static final String KEY_ID_COUPLE = "id";
    private static final String KEY_INGREDIENT_PARENT_ID= "parentId";
    private static final String KEY_INGREDIENT_ID = "ingredientId";
    private static final String[] COLUMNS_COUPLE = {KEY_ID_COUPLE, KEY_INGREDIENT_PARENT_ID, KEY_INGREDIENT_ID};
    // PRESETS
    private static final String TABLE_NAME_PRESETS = "preset";
    private static final String KEY_ID_PRESETS = "id";
    private static final String[] COLUMNS_PRESETS = {KEY_ID_PRESETS, KEY_ENGLISH, KEY_DUTCH, KEY_CHECKED};
    // PRESETS INGREDIENT COUPLE TABLE
    private static final String TABLE_NAME_PRESETS_INGREDIENTS = "presetIngredients";
    private static final String KEY_ID = "id";
    private static final String KEY_PRESET_ID = "presetId";
    private static final String[] COLUMNS_PRESET_INGREDIENT_COUPLE = {KEY_ID, KEY_PRESET_ID, KEY_INGREDIENT_ID};
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
        final String CREATE_COUPLE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_COUPLE + " (" + KEY_ID_COUPLE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_INGREDIENT_PARENT_ID + " INTEGER, " + KEY_INGREDIENT_ID + " INTEGER)";
        // Create preset table
        final String CREATE_PRESETS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_PRESETS + " (" + KEY_ID_PRESETS + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ENGLISH + " TEXT, " + KEY_DUTCH + " TEXT, " + KEY_CHECKED + " BIT)";
        // Create preset ingredient couple table
        final String CREATE_PRESET_INGREDIENT_COUPLE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_PRESETS_INGREDIENTS + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_PRESET_ID + " INTEGER, " + KEY_INGREDIENT_ID + " INTEGER)";
        // Create migration table
        final String CREATE_MIGRATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_MIGRATIONS + " (" + KEY_ID_MIGRATIONS + " INTEGER PRIMARY KEY AUTOINCREMENT, value INT)";

        db.execSQL(CREATE_INGREDIENT_TABLE);
        db.execSQL(CREATE_COUPLE_TABLE);
        db.execSQL(CREATE_PRESETS_TABLE);
        db.execSQL(CREATE_PRESET_INGREDIENT_COUPLE_TABLE);
        db.execSQL(CREATE_MIGRATION_TABLE);

        // Read migrations
        try {
            // to reach asset
            AssetManager assetManager = context.getAssets();
            // to get all item in dogs folder.
            String[] migrations = assetManager.list("migrations");
            InputStream inputStream;
            Log.d("MIGRATIONS", "files = " + migrations.length);
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
                                Log.d("MIGRATIONS", "Skipping migration " + line);
                                continue;
                            }
                            // If not already executed, add it to db
                            Log.d("MIGRATIONS", "Migrating " + line);
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

    //Checks if the migrations is already executed
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

    public void addIngredientToParent(Ingredient parent, Ingredient ingredient){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INGREDIENT_PARENT_ID, parent.Id);
        values.put(KEY_INGREDIENT_ID, ingredient.Id);

        db.insert(TABLE_NAME_COUPLE, null, values);
        db.close();
    }

    public void removeIngredientFromParent(Ingredient parent, Ingredient ingredient){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_COUPLE, KEY_INGREDIENT_PARENT_ID + " = ? && " + KEY_INGREDIENT_ID + " = ?", new String[] {String.valueOf(parent.Id), String.valueOf(ingredient.Id)});
        db.close();
    }

    public void addIngredientToPreset(Ingredient preset, Ingredient ingredient){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRESET_ID, preset.Id);
        values.put(KEY_INGREDIENT_ID, ingredient.Id);

        db.insert(TABLE_NAME_PRESETS_INGREDIENTS, null, values);
        db.close();
    }

    public void removeIngredientFromPreset(Ingredient preset, Ingredient ingredient){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_PRESETS_INGREDIENTS, KEY_PRESET_ID + " = ? && " + KEY_INGREDIENT_ID + " = ?", new String[] {String.valueOf(preset.Id), String.valueOf(ingredient.Id)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INGREDIENTS);
        this.onCreate(db);
    }

    //Presets will be basically the same as the ingredient class. Yet, we only want the users to be able to edit their presets, not the ingredients.
    public void AddPreset(Ingredient preset, ArrayList<Ingredient> ingredients) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ENGLISH, preset.English);
        values.put(KEY_DUTCH, preset.Dutch);

        db.insert(TABLE_NAME_PRESETS, null, values);

        values = new ContentValues();
        for(Ingredient ingredient : ingredients){
            values.put(KEY_ID_PRESETS, preset.Id);
            values.put(KEY_INGREDIENT_ID, ingredient.Id);
            // TODO
            // ADD TO PRESETS/INGREDIENTS TABLE
//            db.insert(TABLE_NAME_PRESETS_INGREDIENTS, null, values);
        }
        db.close();
    }
    public void deleteOnePreset(Ingredient preset){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_PRESETS, KEY_ID_PRESETS + " = ?", new String[] {String.valueOf(preset.Id)});
        db.close();
    }

    public Ingredient getPreset(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_PRESETS,
                COLUMNS_PRESETS,
                KEY_ID_INGREDIENTS + " = ?", // selection
                new String[]{String.valueOf(id)}, // params
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (cursor.getCount() > 0) {
            boolean checked = (Integer.parseInt(cursor.getString(3)) == 1);
            Ingredient ingredient = new Ingredient(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    checked);

            return ingredient;
        }
        return null;
    }

    public ArrayList<Ingredient> getAllPresets(){
        ArrayList<Ingredient> presets = new ArrayList<Ingredient>();
        String query = "SELECT * FROM " + TABLE_NAME_PRESETS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Ingredient preset = null;

        if(cursor.moveToFirst()){
            do {

                boolean checked = (Integer.parseInt(cursor.getString(3)) == 1);
                preset = new Ingredient(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        checked);

                presets.add(preset);
            } while (cursor.moveToNext());
        }

        return presets;
    }

    public void deleteOneIngredient(Ingredient ingredient){
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

    public ArrayList<Ingredient> getAllIngredients(){
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

    public ArrayList<Ingredient> getAllIngredientsFromCheckedPresets(){
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        String query = "SELECT * FROM " + TABLE_NAME_INGREDIENTS +
                " WHERE " + KEY_ID_INGREDIENTS + " IN" +
                " (" +
                "SELECT " + KEY_INGREDIENT_ID + " FROM " + TABLE_NAME_PRESETS_INGREDIENTS +
                " WHERE " + KEY_PRESET_ID + " IN" +
                " (" +
                "SELECT " + KEY_ID_PRESETS + " FROM " + TABLE_NAME_PRESETS +
                " WHERE " + KEY_CHECKED + " == 1"+
                ")" +
                ")";

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

    public ArrayList<Ingredient> getAllIngredientsExcludingParents(){
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        String query = "SELECT * FROM " + TABLE_NAME_INGREDIENTS +
                " WHERE " + KEY_ID_INGREDIENTS + " NOT IN" +
                " (" +
                "SELECT " + KEY_INGREDIENT_PARENT_ID + " FROM " + TABLE_NAME_COUPLE +
                ")";

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

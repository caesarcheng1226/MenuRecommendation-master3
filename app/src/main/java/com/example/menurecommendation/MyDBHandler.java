package com.example.menurecommendation;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;

public class MyDBHandler extends SQLiteOpenHelper {
    private static String DB_PATH;
    private static String DB_NAME = "Recipe.db";
    private SQLiteDatabase RecipeDB;
    private final Context myContext;
    private static final int DB_VERSION = 1;

    // Column Headers
    public static final String CUISINE_TABLE_NAME = "Cuisine";
    public static final String INGREDIENTS_TABLE_NAME = "Ingredients";

    public static final String UNIQUE_ID = "ID";
    public static final String RECIPE_NAME = "RecipeName";
    public static final String COOKING_TIME = "CookingTime";
    public static final String INGREDIENTS = "Ingredients";
    public static final String STEPS = "Steps";
    public static final String DIFFICULTY = "Diffculty";
    public static final String TAG = "Tag";

    //initialize the database
    public MyDBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        DB_PATH = context.getDatabasePath(DB_NAME).toString();
        this.myContext = context;
    }

    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        if(!dbExist){
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try { copyDataBase(); }
            catch (IOException e) { throw new Error("Error copying database"); }
        }
    }

    private boolean checkDataBase(){
        File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.d("database", "Copied");
    }

    public Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:" + myContext.getDatabasePath(DB_NAME);
            DriverManager.registerDriver((Driver) Class.forName("org.sqldroid.SQLDroidDriver").newInstance());
            // create a connection to the database
            conn = DriverManager.getConnection(url);
        }  catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<RecipeDetailsData> findHandlerByTag(String tag) {
        if (tag.length() == 0)
            return null;
        String sql = "SELECT * FROM " + CUISINE_TABLE_NAME + " WHERE " + TAG + " =?";
        Log.d("SQL",sql);
        List<RecipeDetailsData> resultList = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {
            pstmt.setString(1,  tag.substring(0, 1).toUpperCase() + tag.substring(1).toLowerCase());
            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()) {
                RecipeDetailsData foundedData = new RecipeDetailsData();
                foundedData.setID(Integer.parseInt(rs.getString(1)));
                Log.d("DATA", ""+foundedData.getID());
                foundedData.setRecipeName(rs.getString(2));
                foundedData.setCookingTime(Integer.parseInt(rs.getString(3)));
                foundedData.setIngredients(rs.getString(4));
                foundedData.setSteps(rs.getString(5));
                foundedData.setDifficulty(Integer.parseInt(rs.getString(6)));
                foundedData.setTags(rs.getString(7));
                resultList.add(foundedData);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        if (resultList.size() == 0)
            return null;
        return resultList;
    }

//    public List<RecipeDetailsData> findIngredients(int RecipeID) {
//        String sql = "SELECT * FROM " + CUISINE_TABLE_NAME + " JOIN " + INGREDIENTS_TABLE_NAME +
//                " ON "" WHERE " + TAG + " =?";
//        Log.d("SQL",sql);
//        List<RecipeDetailsData> resultList = new ArrayList<>();
//        try (Connection conn = this.connect();
//             PreparedStatement pstmt  = conn.prepareStatement(sql)) {
//            pstmt.setString(1,  tag.substring(0, 1).toUpperCase() + tag.substring(1).toLowerCase());
//            ResultSet rs  = pstmt.executeQuery();
//            while (rs.next()) {
//                RecipeDetailsData foundedData = new RecipeDetailsData();
//                foundedData.setID(Integer.parseInt(rs.getString(1)));
//                Log.d("DATA", ""+foundedData.getID());
//                foundedData.setRecipeName(rs.getString(2));
//                foundedData.setCookingTime(Integer.parseInt(rs.getString(3)));
//                foundedData.setIngredients(rs.getString(4));
//                foundedData.setSteps(rs.getString(5));
//                foundedData.setDifficulty(Integer.parseInt(rs.getString(6)));
//                foundedData.setTags(rs.getString(7));
//                resultList.add(foundedData);
//            }
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//        if (resultList.size() == 0)
//            return null;
//        return resultList;
//    }
}

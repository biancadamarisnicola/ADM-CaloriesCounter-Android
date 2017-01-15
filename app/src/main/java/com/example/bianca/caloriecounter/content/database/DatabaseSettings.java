package com.example.bianca.caloriecounter.content.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.bianca.caloriecounter.content.Aliment;
import com.example.bianca.caloriecounter.content.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bianca on 19.11.2016.
 */

public class DatabaseSettings extends SQLiteOpenHelper {
    private static final String TAG = DatabaseSettings.class.getSimpleName();

    public static final String TABLE_USERS = "users";
    public static final String TABLE_ALIMENTS = "aliments";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_PROTEINS = "proteins";
    public static final String COLUMN_FATS = "fats";
    public static final String COLUMN_CARBS = "carbs";
    public static final String COLUMN_TOKEN = "token";

    private static final String DATABASE_NAME = "caloriescounter.db";
    private static final int DATABASE_VERSION = 2;
    private static final String USER_TABLE_CREATE = "create table "
            + TABLE_USERS + "( " + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_USERNAME + " text not null, "
            + COLUMN_PASSWORD + " text not null, "
            + COLUMN_TOKEN + " text not null);";

    private static final String ALIMENT_TABLE_CREATE = "create table "
            + TABLE_ALIMENTS + "( " + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_CALORIES + " text not null, "
            + COLUMN_PROTEINS + " text not null, "
            + COLUMN_CARBS + " text not null, "
            + COLUMN_FATS + " text not null);";

    public DatabaseSettings(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "DatabaseSettings");

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(TAG, "onCreate Database");
        database.execSQL(USER_TABLE_CREATE);
        database.execSQL(ALIMENT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpdate Database");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALIMENTS);
        onCreate(db);
    }

    public void saveUser(User user) {
        Log.d(TAG, "saveUser Database; token " + user.getToken());
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TOKEN, user.getToken());
        cv.put(COLUMN_USERNAME, user.getUsername());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_USERS, null, cv);
        db.close();
    }

    public void saveAliment(Aliment aliment) {
        Log.d(TAG, "saveAliment Database");
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, aliment.getName());
        cv.put(COLUMN_CALORIES, aliment.getCalories());
        cv.put(COLUMN_PROTEINS, aliment.getProteins());
        cv.put(COLUMN_CARBS, aliment.getCarbs());
        cv.put(COLUMN_FATS, aliment.getFats());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ALIMENTS, null, cv);
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public User getCurrentUser() {
        Log.d(TAG, "getCurrentUser Database");
        Cursor c = getReadableDatabase().rawQuery("select token from " + TABLE_USERS, null, null);
        if (c.moveToFirst()) {
            return new User(null, null, c.getString(0));
        } else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public List<Aliment> getAliments() {
        Log.d(TAG, "getAliments Database");
        List<Aliment> aliments = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("select * from " + TABLE_ALIMENTS, null, null);
        int size = c.getCount();
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            aliments.add(new Aliment(c.getString(1), Double.valueOf(c.getString(2)), Double.valueOf(c.getString(3)), Double.valueOf(c.getString(4)), Double.valueOf(c.getString(5))));
            c.moveToNext();
        }
        ;
        return aliments;
    }

    public void deleteAliment(Aliment aliment) {
        Log.d(TAG, "deleteAliment Database");
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, aliment.getName());
        String whereClause = " name =? ";
        String[] args = new String[1];
        args[0] = aliment.getName();
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ALIMENTS, whereClause, args);
        db.close();
    }

    public Aliment getAliment(String alimentName) {
        Log.d(TAG, "get aliment with name " + alimentName);
        Cursor c = getReadableDatabase().rawQuery("select * from " + TABLE_ALIMENTS +  " WHERE name = ?", new String[] { alimentName });
        c.moveToFirst();
        if (c.moveToFirst()) {
            Log.d(TAG, c.getString(0) + c.getString(1));
            return new Aliment(c.getString(1), Double.valueOf(c.getString(2)), Double.valueOf(c.getString(3)), Double.valueOf(c.getString(4)), Double.valueOf(c.getString(5)));
        } else {
            return null;
        }
    }

    public void deleteAllAliments() {
        getWritableDatabase().execSQL("DELETE FROM " + TABLE_ALIMENTS + ";");
    }
}

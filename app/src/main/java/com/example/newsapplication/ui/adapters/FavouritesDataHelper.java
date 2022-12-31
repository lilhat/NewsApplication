package com.example.newsapplication.ui.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FavouritesDataHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "favourites.db";
    public static final String TABLE_NAME = "favouritesInfo";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_SOURCE = "source";
    public static final String COL_COUNTRY = "country";
    public static final String COL_CATEGORY = "category";
    public static final String COL_TIME = "time";
    public static final String COL_TEXT = "text";
    public static final String COL_IMG = "image";


    public FavouritesDataHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, "
                + COL_TITLE + " text not null, "
                + COL_SOURCE + " text not null, "
                + COL_COUNTRY + " text not null, "
                + COL_CATEGORY + " text not null, "
                + COL_TIME + " text not null, "
                + COL_TEXT + " text not null, "
                + COL_IMG + " text not null);";

        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertFavouriteData(String title, String source, String country, String category, String time, String text, String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_SOURCE, source);
        contentValues.put(COL_COUNTRY, country);
        contentValues.put(COL_CATEGORY, category);
        contentValues.put(COL_TIME, time);
        contentValues.put(COL_TEXT, text);
        contentValues.put(COL_IMG, image);
        long res = db.insert(TABLE_NAME, null, contentValues);
        return res!= -1;

    }

    public Cursor getFavouriteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COL_TIME + " DESC"
        );
    }
}

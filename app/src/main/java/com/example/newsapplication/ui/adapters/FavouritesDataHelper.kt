package com.example.newsapplication.ui.adapters

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FavouritesDataHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    init {
        val db = this.writableDatabase
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase) {
        val DATABASE_CREATE = ("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, "
                + COL_TITLE + " text not null, "
                + COL_SOURCE + " text not null, "
                + COL_CREATOR + " text not null, "
                + COL_COUNTRY + " text not null, "
                + COL_CATEGORY + " text not null, "
                + COL_TIME + " text not null, "
                + COL_TEXT + " text not null, "
                + COL_LINK + " text not null, "
                + COL_IMG + " text not null);")
        db.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("drop table if exists $TABLE_NAME")
        onCreate(db)
    }

    fun insertFavouriteData(
        title: String?,
        source: String?,
        creator: String?,
        country: String?,
        category: String?,
        time: String?,
        text: String?,
        link: String?,
        image: String?
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_TITLE, title)
        contentValues.put(COL_SOURCE, source)
        contentValues.put(COL_CREATOR, creator)
        contentValues.put(COL_COUNTRY, country)
        contentValues.put(COL_CATEGORY, category)
        contentValues.put(COL_TIME, time)
        contentValues.put(COL_TEXT, text)
        contentValues.put(COL_LINK, link)
        contentValues.put(COL_IMG, image)
        val res = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return res != -1L
    }

    val favouriteData: Cursor
        get() {
            val db = this.writableDatabase
            return db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "$COL_TIME DESC"
            )
        }


    fun checkData(title: String?): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("select * from $TABLE_NAME where $COL_TITLE=?", arrayOf(title))
        return cursor.count != 0
    }

    fun removeData(title: String?){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_TITLE=?", arrayOf(title))
    }

    companion object {
        const val DB_NAME = "favourites.db"
        const val TABLE_NAME = "favouritesInfo"
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_SOURCE = "source"
        const val COL_CREATOR = "creator"
        const val COL_COUNTRY = "country"
        const val COL_CATEGORY = "category"
        const val COL_TIME = "time"
        const val COL_TEXT = "text"
        const val COL_LINK = "link"
        const val COL_IMG = "image"
    }
}
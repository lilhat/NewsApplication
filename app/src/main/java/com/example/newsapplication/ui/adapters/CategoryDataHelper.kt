package com.example.newsapplication.ui.adapters

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CategoryDataHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    init {
        val db = this.writableDatabase
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase) {
        val DATABASE_CREATE = ("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, "
                + COL_EMAIL + " text not null, "
                + COL_BUS + " integer not null, "
                + COL_ENT + " integer not null, "
                + COL_ENV + " integer not null, "
                + COL_FOO + " integer not null, "
                + COL_HEA + " integer not null, "
                + COL_WOR + " integer not null, "
                + COL_TOP + " integer not null, "
                + COL_POL + " integer not null, "
                + COL_SPO + " integer not null, "
                + COL_SCI + " integer not null, "
                + COL_TEC + " integer not null);")
        db.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("drop table if exists $TABLE_NAME")
        onCreate(db)
    }

    fun insertFavouriteData(
        email: String,
        business: Int,
        entertainment: Int,
        environment: Int,
        food: Int,
        health: Int,
        politics: Int,
        science: Int,
        sports: Int,
        technology: Int,
        top: Int,
        world: Int,
    ): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_EMAIL, email)
        contentValues.put(COL_BUS, business)
        contentValues.put(COL_ENT, entertainment)
        contentValues.put(COL_ENV, environment)
        contentValues.put(COL_FOO, food)
        contentValues.put(COL_HEA, health)
        contentValues.put(COL_WOR, world)
        contentValues.put(COL_TOP, top)
        contentValues.put(COL_POL, politics)
        contentValues.put(COL_SPO, sports)
        contentValues.put(COL_SCI, science)
        contentValues.put(COL_TEC, technology)

        val res = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return res != -1L
    }

    val categoryData: Cursor
        get() {
            val db = this.writableDatabase
            return db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
            )
        }


    fun checkData(email: String?): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("select * from $TABLE_NAME where $COL_EMAIL=?", arrayOf(email))
        return cursor.count != 0
    }

    fun removeData(email: String?){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_EMAIL=?", arrayOf(email))
    }

    companion object {
        const val DB_NAME = "categories.db"
        const val TABLE_NAME = "categoriesInfo"
        const val COL_ID = "id"
        const val COL_EMAIL = "email"
        const val COL_BUS = "business"
        const val COL_ENT = "entertainment"
        const val COL_ENV = "environment"
        const val COL_FOO = "food"
        const val COL_HEA = "health"
        const val COL_WOR = "world"
        const val COL_TOP = "top"
        const val COL_POL = "politics"
        const val COL_SPO = "sports"
        const val COL_SCI = "science"
        const val COL_TEC = "technology"

    }
}
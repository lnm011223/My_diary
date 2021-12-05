package com.lnm011223.my_diary

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val context: Context, name: String, version: Int): SQLiteOpenHelper(context,name,null,version) {

    private val account_creat = "create table diarydata (" +
            "id integer primary key autoincrement," +
            "datetext text," +
            "moodid integer," +
            "imageuri text," +
            "diarytext text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(account_creat)
        Toast.makeText(context,"Create succeeded",Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
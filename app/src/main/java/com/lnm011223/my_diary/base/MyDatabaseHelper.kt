package com.lnm011223.my_diary.base

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

// TODO: 改善时间存储方式 
class MyDatabaseHelper(val context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {

    private val diray_create = "create table diarydata (" +
            "id integer primary key autoincrement," +
            "datetext text unique," +
            "moodid integer," +
            "imageuri text," +
            "diarytext text)"

    private val todo_create = "create table tododata (" +
            "id integer primary key autoincrement," +
            "todotext text," +
            "classification text," +
            "startdate text," +
            "enddate text," +
            "deadline text," +
            "isTop integer," +
            "isDone integer)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(diray_create)
        db.execSQL(todo_create)
        Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
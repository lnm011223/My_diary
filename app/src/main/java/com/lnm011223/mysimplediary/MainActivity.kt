package com.lnm011223.mysimplediary

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.diary_layout.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val diarylist = ArrayList<Diary>()
    val dbHelper = MyDatabaseHelper(this,"DiaryData.db",1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        add_button.setOnClickListener {
            val intent = Intent(this,AddActivity::class.java)
            startActivity(intent)
        }
        listview.setOnItemClickListener { parent, view, position, id ->
            val diary = diarylist[position]
            val intent = Intent(this,CheckActivity::class.java)
            intent.putExtra("diarytext",diary.diarytext)
            startActivity(intent)

        }
    }

    override fun onStart() {
        super.onStart()
        val adapter = DiaryAdapter(this,R.layout.diary_item,diarylist)
        listview.adapter = adapter
        val db = dbHelper.writableDatabase
        initDiarys()

    }
    @SuppressLint("Range")
    private fun initDiarys(){

        thread {
            diarylist.clear()
            val db = dbHelper.writableDatabase
            val cursor = db.query("diarydata",null,null,null,null,null,null,null)
            if (cursor.moveToFirst()) {
                do {
                    val datetext = cursor.getString(cursor.getColumnIndex("datetext"))

                    val diarytext = cursor.getString(cursor.getColumnIndex("diarytext"))
                    diarylist.add(Diary(datetext,diarytext))
                }while (cursor.moveToNext())
            }
            cursor.close()
        }

    }
}
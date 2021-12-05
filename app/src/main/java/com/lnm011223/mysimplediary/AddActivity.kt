package com.lnm011223.mysimplediary

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        datetext.text = SimpleDateFormat("MM 月 dd 日 E").format(Date())
        complete_button.setOnClickListener {
            val diary_text = diary_edit.text.toString()
            val dbHelper = MyDatabaseHelper(this,"DiaryData.db",1)
            val db = dbHelper.writableDatabase
            val diary_value = ContentValues().apply {
                put("datetext", datetext.text as String)
                put("diarytext",diary_text)
            }
            db.insert("diarydata",null,diary_value)
            finish()
        }
    }
}
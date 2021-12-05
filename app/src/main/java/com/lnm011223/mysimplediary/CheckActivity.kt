package com.lnm011223.mysimplediary

import android.app.AlertDialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_check.*

class CheckActivity : AppCompatActivity() {
    var diarytext = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        diarytext = intent.getStringExtra("diarytext").toString()
        val dbHelper = MyDatabaseHelper(this,"DiaryData.db",1)
        val db = dbHelper.writableDatabase
        val diarytext_backup = diarytext
        diary_edit1.setText(diarytext)
        reset_button.setOnClickListener {
            diarytext = diary_edit1.text.toString()
            val diary_value = ContentValues().apply {


                put("diarytext",diarytext)
            }
            db.update("diarydata",diary_value,"diarytext = ?", arrayOf(diarytext_backup))
            finish()
        }
        delete_button.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("确认：")
                setMessage("真的要删除这条记录吗？")
                setNegativeButton("否") { _, _ ->


                }
                setPositiveButton("是") { _, _ ->
                    db.delete("diarydata", "diarytext = ?", arrayOf(diarytext_backup))
                    finish()


                }


                show()
            }
        }
    }
}
package com.lnm011223.my_diary.ui.revise

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.databinding.ActivityReviseTodoBinding
import com.lnm011223.my_diary.logic.model.Diary
import com.lnm011223.my_diary.logic.model.Todo
import com.lnm011223.my_diary.util.BaseUtil
import java.text.SimpleDateFormat
import java.util.*

class ReviseTodoActivity : AppCompatActivity() {
    private var secondNum: Long = 0

    private lateinit var binding: ActivityReviseTodoBinding
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviseTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
        val db = dbHelper.writableDatabase
        BaseUtil.rightColor(window, this)
        binding.dateText.text = SimpleDateFormat("MM 月 dd 日 E").format(Date())
        binding.picker.setOnDateTimeChangedListener { millisecond ->
            secondNum = millisecond

        }
        val position = intent.getStringExtra("position")
        val todo = intent.getParcelableExtra<Todo>("todo") as Todo
        binding.todotextEdit.setText(todo.todoText)
        binding.classtextEdit.setText(todo.classification)
        binding.isTopButton.isChecked = when (todo.isTop){
            1 -> true
            else -> false
        }
        binding.completeButton.setOnClickListener {
            val todotext = binding.todotextEdit.text.toString()
            val deadline = BaseUtil.second2Date(secondNum)
            val classification = binding.classtextEdit.text.toString()
            val isTop = if (binding.isTopButton.isChecked) {
                1
            } else {
                0
            }
            val todo_value = ContentValues().apply {
                put("todotext", todotext)
                put("classification", classification)
                put("deadline", deadline)
                put("isTop", isTop)
            }
            db.update("tododata", todo_value, "id = ?", arrayOf(todo.id.toString()))
            val intentResult = Intent()
            val reviseItem = Todo(
                todo.id,
                todotext,
                classification,
                todo.startDate,
                todo.endDate,
                deadline,
                isTop,
                0

            )
            intentResult.apply {
                putExtra("todoPosition", position)
                putExtra("reviseTodo", reviseItem)
            }
            setResult(RESULT_OK, intentResult)
            onBackPressed()
        }


    }


}
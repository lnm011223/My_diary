package com.lnm011223.my_diary.ui.add

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.lnm011223.my_diary.databinding.ActivityAddTodoBinding
import com.lnm011223.my_diary.util.BaseUtil
import com.lnm011223.my_diary.base.BaseActivity
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.logic.model.Diary
import com.lnm011223.my_diary.logic.model.Todo
import java.text.SimpleDateFormat
import java.util.*

class AddTodoActivity : BaseActivity() {
    private lateinit var binding: ActivityAddTodoBinding
    private var secondNum: Long = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
        dbHelper.writableDatabase
        BaseUtil.rightColor(window, this)
        binding.dateText.text = SimpleDateFormat("MM 月 dd 日 E").format(Date())
        binding.picker.setOnDateTimeChangedListener { millisecond ->
            secondNum = millisecond

        }

        binding.completeButton.setOnClickListener { view ->
            Log.d("millisecond", BaseUtil.second2Date(secondNum))
            val startdate = BaseUtil.second2Date(System.currentTimeMillis())
            val todotext = binding.todotextEdit.text.toString()
            val deadline = BaseUtil.second2Date(secondNum)
            val classification = binding.classtextEdit.text.toString()
            val isTop = if (binding.isTopButton.isChecked) {
                1
            } else {
                0
            }
            val db = dbHelper.writableDatabase
            val todo_value = ContentValues().apply {
                put("todotext", todotext)
                put("classification", classification)
                put("deadline", deadline)
                put("startdate", startdate)
                put("isTop", isTop)
            }
            val success = db.insert("tododata", null, todo_value)
            Log.d("successtest", success.toString())
            val id = success.toString().toInt()


            val intent = Intent()
            val location = IntArray(2)
            view.getLocationInWindow(location)
            //把点击按钮的中心位置坐标传过去作为 AddDiaryActivity 的揭露动画圆心
            intent.putExtra(CLICK_X, location[0] + view.width / 2)
            intent.putExtra(CLICK_Y, location[1] + view.height / 2)
            if (id != -1) {
                Log.d("successtest", success.toString())
                intent.putExtra(
                    "addTodo",
                    Todo(id, todotext, classification, startdate, "0", deadline, isTop, 0)
                )

                setResult(RESULT_OK, intent)
                super.onBackPressed()
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("提醒：")
                    setMessage("创建失败，请检查是否有选项为空！")

                    setPositiveButton("是") { _, _ ->
                        super.onBackPressed()

                    }


                    show()
                }
            }
        }
    }


}
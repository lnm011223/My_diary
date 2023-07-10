package com.lnm011223.my_diary.logic

import android.annotation.SuppressLint
import androidx.lifecycle.liveData
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.logic.model.Todo
import kotlinx.coroutines.Dispatchers

/**

 * @Author liangnuoming
 * @Date 2023/7/9-18:44

 */
/// TODO: 优化成MVVM架构
object Repository {
    @SuppressLint("StaticFieldLeak")
    val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
    @SuppressLint("Range")
    fun initTodoList() = liveData(Dispatchers.IO) {
        val result = try {
            val finishedList = ArrayList<Todo>()
            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("select * from tododata ", null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex("id")).toInt()
                    val todotext = cursor.getString(cursor.getColumnIndex("todotext"))
                    val classification = cursor.getString(cursor.getColumnIndex("classification"))
                    val startdate = cursor.getString(cursor.getColumnIndex("startdate"))
                    val deadline = cursor.getString(cursor.getColumnIndex("deadline"))
                    val isTop = cursor.getInt(cursor.getColumnIndex("isTop"))
                    val isDone = cursor.getInt(cursor.getColumnIndex("isDone"))
                    val enddate = cursor.getString(cursor.getColumnIndex("enddate"))
                    when (isDone) {
                        0 -> {
                            when (isTop) {
                                0 -> {
                                }
                                1 -> {


                                }
                            }
                        }
                        1 -> {
                            when (isTop) {
                                0 -> {
                                    finishedList.add(
                                        Todo(
                                            id,
                                            todotext,
                                            classification,
                                            startdate,
                                            enddate,
                                            deadline,
                                            isTop,
                                            0
                                        )
                                    )
                                }
                                1 -> {
                                    finishedList.add(
                                        0,
                                        Todo(
                                            id,
                                            todotext,
                                            classification,
                                            startdate,
                                            enddate,
                                            deadline,
                                            isTop,
                                            0
                                        )
                                    )
                                }
                            }
                        }
                    }


                } while (cursor.moveToNext())
            }
            cursor.close()
            Result.success(finishedList)
        } catch (e: Exception) {
            Result.failure<ArrayList<Todo>>(e)
        }
        emit(result)
    }


}
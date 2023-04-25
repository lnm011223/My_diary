package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.logic.model.Todo
import com.lnm011223.my_diary.util.BaseUtil
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**

 * @Author liangnuoming
 * @Date 2023/4/25-02:26

 */
class NotifyWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Recycle", "Range")
    override fun doWork(): Result {
        Log.d(TAG,"已运行！")
        try {
            // 读取待办事项数据
            val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("select * from tododata ", null)
            val unfinshedList = ArrayList<Todo>()
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
                            unfinshedList.add(
                                Todo(
                                    id,
                                    todotext,
                                    classification,
                                    startdate,
                                    "0",
                                    deadline,
                                    isTop,
                                    0
                                )
                            )

                        }
                        1 -> {
                        }
                    }



                } while (cursor.moveToNext())
            }
            cursor.close()

            // 遍历待办事项，如果距离过期时间不足15分钟，就发送通知
            for (todo in unfinshedList) {
                val compare = todo.deadline.toBigInteger() - BaseUtil.second2Date(System.currentTimeMillis()).toBigInteger()
                if ("0".toBigInteger() < compare && "15".toBigInteger() > compare) {
                    sendNotification(todo)
                }
            }
            return Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Error checking and notifying todos.", e)
            return Result.failure()
        }
    }

    // 发送通知
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(todo: Todo) {
        val channelId = "todo_channel"
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.twotone_notifications_24)
            .setContentTitle("您有一个待办事项已不足15分钟：")
            .setContentText("${todo.todoText} - 截止时间：${BaseUtil.day2Text(todo.deadline)}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("todo_channel", "todo", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(todo.id,notificationBuilder)



    }



    companion object {
        private const val TAG = "NotifyWorker"
        private val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    }
}



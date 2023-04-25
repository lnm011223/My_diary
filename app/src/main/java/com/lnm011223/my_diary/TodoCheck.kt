package com.lnm011223.my_diary

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit

/**

 * @Author liangnuoming
 * @Date 2023/4/25-15:28

 */
class TodoCheck {

    // 这里使用伴生对象，方便其他类调用
    companion object {
        private const val TAG = "TodoCheck"

        fun start(context: Context) {
            Log.d(TAG,"todocheck已运行")
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val notifyWork =
                PeriodicWorkRequestBuilder<NotifyWorker>(15, TimeUnit.MINUTES)
                    .addTag(TAG)
//                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                notifyWork
            )
        }
    }
}
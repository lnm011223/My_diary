package com.lnm011223.my_diary

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lnm011223.my_diary.base.MyApplication.Companion.context
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.databinding.ActivityMainBinding
import com.lnm011223.my_diary.ui.add.AddDiaryActivity
import com.lnm011223.my_diary.ui.add.AddTodoActivity
import com.lnm011223.my_diary.util.BaseUtil
import com.lnm011223.my_diary.util.DensityUtil
import com.lnm011223.my_diary.base.BaseActivity.Companion.CLICK_X
import com.lnm011223.my_diary.base.BaseActivity.Companion.CLICK_Y
import com.lnm011223.my_diary.base.MyApplication
import com.permissionx.guolindev.PermissionX
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val dbHelper = MyDatabaseHelper(context, "DiaryData.db", 1)
        dbHelper.writableDatabase
        //控制两个BottomNavigationView
        val navView: BottomNavigationView = binding.navView
        val navView2: BottomNavigationView = binding.navView2
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        navView2.setupWithNavController(navController)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {

            setDisplayShowTitleEnabled(false)
        }
        getPermission(this)
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        val isNotice = prefs?.getBoolean("isNotice", false)
        if (isNotice!!) {
            setDailyNotification()
        }
        TodoCheck.start(this)
        BaseUtil.rightColor(window, this)
        //控制不同fragment时fab的点击
        binding.fab.setOnClickListener { view ->
            when (navController.currentDestination?.id) {
                R.id.navigation_dashboard -> {
                    val intent = Intent(this, AddDiaryActivity::class.java)
                    circleLocation(view, intent)
                    startActivityForResult(intent, 1)
                }
                R.id.navigation_home -> {
                    val intent = Intent(this, AddTodoActivity::class.java)
                    circleLocation(view, intent)
                    startActivityForResult(intent, 3)
                }
            }

        }
        //控制BottomNavigationView和fab的效果
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    binding.toolbarTitle.text = "待办事项"
                    showFab(window, binding)
                    binding.fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_all_24))
                    binding.fab.show()
                }
                R.id.navigation_dashboard -> {
                    binding.toolbarTitle.text = "日记记录"

                    showFab(window, binding)
                    binding.fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_add_24))
                    binding.fab.show()
                }
                R.id.navigation_charts -> {
                    binding.toolbarTitle.text = "图表分析"

                    window.statusBarColor = ContextCompat.getColor(context, R.color.backgroundcolor)
                    hideFab(binding)
                }

                R.id.navigation_settings -> {
                    binding.toolbarTitle.text = "个人设置"

                    window.statusBarColor = ContextCompat.getColor(context, R.color.backgroundcolor)
                    hideFab(binding)
                }
            }
        }

    }

    //作为接收recyclerview添加以及修改结果的中转站
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                mainViewModel.addDiaryItem = data?.getParcelableExtra("addDiary")!!
                mainViewModel.addPosition.value = 1
            }
            2 -> {
                if (data?.getStringExtra("position").toString() != "null") {
                    mainViewModel.reviseDiaryItem = data?.getParcelableExtra("reviseDiary")!!
                    mainViewModel.setPosition(
                        position = data.getStringExtra("position")?.toInt() ?: -1
                    )
                }

            }
            3 -> if (resultCode == RESULT_OK) {
                mainViewModel.addunfinishedItem = data?.getParcelableExtra("addTodo")!!
                mainViewModel.addTodoPosition.value = 1
            }

            4 -> {
                if (data?.getStringExtra("todoPosition").toString() != "null") {
                    mainViewModel.reviseTodoItem = data?.getParcelableExtra("reviseTodo")!!
                    mainViewModel.setTodoPosition(
                        position = data.getStringExtra("todoPosition")?.toInt() ?: -1
                    )
                }
            }
        }
    }


    private fun circleLocation(view: View, intent: Intent) {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        //把点击按钮的中心位置坐标传过去作为 AddDiaryActivity 的揭露动画圆心
        intent.putExtra(CLICK_X, location[0] + view.width / 2)
        intent.putExtra(CLICK_Y, location[1] + view.height / 2)
    }

    private fun showFab(window: Window, binding: ActivityMainBinding) {
        window.statusBarColor = ContextCompat.getColor(context, R.color.backgroundcolor)
        binding.navView.updatePadding(right = DensityUtil.dip2px(context, 50f))
        binding.navView2.updatePadding(left = DensityUtil.dip2px(context, 50f))
        binding.bottomAppBar.fabAlignmentMode = FAB_ALIGNMENT_MODE_CENTER
        binding.navView2.menu.findItem(R.id.uncheckedItem).isChecked = true

    }

    private fun hideFab(binding: ActivityMainBinding) {
        binding.navView.setPadding(0)
        binding.navView2.setPadding(0)
        binding.navView.menu.findItem(R.id.uncheckedItem).isChecked = true
        binding.fab.hide()
    }

    private fun getPermission(activity: FragmentActivity) {
        PermissionX.init(activity)
            .permissions(
                PermissionX.permission.POST_NOTIFICATIONS,
                Manifest.permission.INTERNET,
                Manifest.permission.VIBRATE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.USE_BIOMETRIC,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.ACCESS_NETWORK_STATE
            )
            .onExplainRequestReason { scope, deniedList ->
                val message = "PermissionX需要您同意以下权限才能正常使用"
                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
//                    Toast.makeText(activity, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun setDailyNotification() {
        // 获取系统的日期和时间
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        // 从 SharedPreferences 中获取用户设置的通知触发时间
        val sharedPreferences = getSharedPreferences("NotificationTime", Context.MODE_PRIVATE)
        val hour = sharedPreferences.getInt("hour", 22)
        val minute = sharedPreferences.getInt("minute", 30)

        // 设置通知触发的时间
        calendar.set(Calendar.HOUR_OF_DAY, hour) // 设定小时为用户设置的小时数
        calendar.set(Calendar.MINUTE, minute) // 设定分钟为用户设置的分钟数
        calendar.set(Calendar.SECOND, 0) // 设定秒为 0 秒

        // 如果当前时间已经过了通知触发时间，那么将通知的触发时间改为明天的对应时间
        if (System.currentTimeMillis() > calendar.timeInMillis) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }


        // 创建通知
        val notificationId = 1

        // 使用 AlarmManager 定时发送通知
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                notificationId,
                Intent(this, MyReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        // 提示用户已成功设置每日提醒
//        Toast.makeText(this, "已设置每日提醒！", Toast.LENGTH_SHORT).show()
    }

}

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        showNotification(context)
    }

    private fun showNotification(context: Context?) {
        Log.d("yes", "yes")
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("normal", "Normal", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        val resultIntent = Intent(MyApplication.context, AddDiaryActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            MyApplication.context,
            0,
            resultIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, "normal")
            .setContentTitle("每日提醒：")
            .setContentText("今天也要记得写日记哦！")
            .setSmallIcon(R.drawable.twotone_notifications_24)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .build()

        notificationManager.notify(1, notification)
    }
}





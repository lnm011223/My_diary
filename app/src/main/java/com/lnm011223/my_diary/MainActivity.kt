package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.lnm011223.my_diary.databinding.ActivityMainBinding
import com.xiaofeidev.appreveal.base.BaseActivity.Companion.CLICK_X
import com.xiaofeidev.appreveal.base.BaseActivity.Companion.CLICK_Y


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var image_uri = ""
    var mood_id:Int = R.drawable.mood_1
    var datetext:String = ""
    var diarytext:String = ""

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)
        dbHelper.writableDatabase

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //val appBarConfiguration = AppBarConfiguration(
        //    setOf(
        //        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
        //    )
        //)
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )

        window.statusBarColor = ContextCompat.getColor(MyApplication.context,R.color.backgroundcolor)
        window.navigationBarColor = ContextCompat.getColor(MyApplication.context,R.color.backgroundcolor)
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        if (!isDarkTheme(this)){

            //insetsController?.isAppearanceLightStatusBars = true
            //insetsController?.isAppearanceLightNavigationBars = true
            insetsController.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true


            }

        }
        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, AddActivity::class.java)
            val location = IntArray(2)
            view.getLocationInWindow(location)
            //把点击按钮的中心位置坐标传过去作为 AddActivity 的揭露动画圆心
            intent.putExtra(CLICK_X, location[0] + view.width/2)
            intent.putExtra(CLICK_Y, location[1] + view.height/2)

            startActivityForResult(intent,1)
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.navigation_home -> {

                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                image_uri = data?.getStringExtra("image_uri").toString()
                mood_id = data?.getIntExtra("mood_id",R.drawable.mood_1)!!
                datetext = data.getStringExtra("date_text").toString()
                diarytext = data.getStringExtra("diary_text1").toString()

            }
        }
    }
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
}



package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
import com.lnm011223.my_diary.MyApplication.Companion.context
import com.lnm011223.my_diary.databinding.ActivityMainBinding
import com.xiaofeidev.appreveal.base.BaseActivity.Companion.CLICK_X
import com.xiaofeidev.appreveal.base.BaseActivity.Companion.CLICK_Y


class MainActivity : AppCompatActivity() {
    lateinit var diaryViewModel: DiaryViewModel
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        diaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)
        val dbHelper = MyDatabaseHelper(context,"DiaryData.db",1)
        dbHelper.writableDatabase
        //控制两个BottomNavigationView
        val navView: BottomNavigationView = binding.navView
        val navView2 : BottomNavigationView = binding.navView2
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        navView2.setupWithNavController(navController)
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        navView2.setupWithNavController(navController)

        //状态栏和导航栏沉浸
        window.statusBarColor = ContextCompat.getColor(context,R.color.backgroundcolor)
        window.navigationBarColor = ContextCompat.getColor(context,R.color.backgroundcolor)
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
        if (!isDarkTheme(this)){

            //insetsController?.isAppearanceLightStatusBars = true
            //insetsController?.isAppearanceLightNavigationBars = true
            insetsController.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true


            }

        }
        //控制不同fragment时fab的点击
        binding.fab.setOnClickListener { view ->
            when (navController.currentDestination?.id) {
                R.id.navigation_dashboard -> {
                    val intent = Intent(this, AddActivity::class.java)
                    val location = IntArray(2)
                    view.getLocationInWindow(location)
                    //把点击按钮的中心位置坐标传过去作为 AddActivity 的揭露动画圆心
                    intent.putExtra(CLICK_X, location[0] + view.width/2)
                    intent.putExtra(CLICK_Y, location[1] + view.height/2)
                    startActivityForResult(intent,1)
                }
            }

        }
        //控制BottomNavigationView和fab的效果
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                R.id.navigation_home -> {
                    binding.navView.updatePadding(right = DensityUtil.dip2px(context,50f))
                    binding.navView2.updatePadding(left = DensityUtil.dip2px(context,50f))
                    binding.bottomAppBar.fabAlignmentMode = FAB_ALIGNMENT_MODE_CENTER
                    binding.navView2.menu.findItem(R.id.uncheckedItem).isChecked = true
                    binding.fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_all_24))
                    binding.fab.show()
                }
                R.id.navigation_dashboard -> {
                    binding.fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_add_24))
                    binding.navView.updatePadding(right = DensityUtil.dip2px(context,50f))
                    binding.navView2.updatePadding(left = DensityUtil.dip2px(context,50f))
                    binding.navView2.menu.findItem(R.id.uncheckedItem).isChecked = true
                    binding.bottomAppBar.fabAlignmentMode = FAB_ALIGNMENT_MODE_CENTER
                    binding.fab.show()
                }
                R.id.navigation_charts -> {
                    binding.navView.setPadding(0)
                    binding.navView2.setPadding(0)
                    binding.navView.menu.findItem(R.id.uncheckedItem).isChecked = true
                    binding.fab.hide()
                }

                R.id.navigation_settings -> {
                    binding.navView.setPadding(0)
                    binding.navView2.setPadding(0)
                    binding.navView.menu.findItem(R.id.uncheckedItem).isChecked = true
                    binding.fab.hide()
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
                diaryViewModel.addDiaryItem = data?.getParcelableExtra("addDiary")!!
                diaryViewModel.addPosition.value = 1
            }
            2 ->  {
                if (data?.getStringExtra("position").toString() != "null"){
                    diaryViewModel.reviseDiaryItem = data?.getParcelableExtra<Diary>("reviseDiary")!!
                    diaryViewModel.setPosition(position = data.getStringExtra("position")?.toInt() ?: -1)
                }

            }
        }
    }
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
}



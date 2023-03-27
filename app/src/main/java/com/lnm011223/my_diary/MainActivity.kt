package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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


class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

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
                    showFab(window, binding)
                    binding.fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_done_all_24))
                    binding.fab.show()
                }
                R.id.navigation_dashboard -> {
                    showFab(window, binding)
                    binding.fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_add_24))
                    binding.fab.show()
                }
                R.id.navigation_charts -> {
                    window.statusBarColor = ContextCompat.getColor(context, R.color.backgroundcolor)
                    hideFab(binding)
                }

                R.id.navigation_settings -> {
                    window.statusBarColor = ContextCompat.getColor(context, R.color.main)
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


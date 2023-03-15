package com.lnm011223.my_diary.ui.add

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.R
import com.xiaofeidev.appreveal.base.BaseActivity

class AddTodoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        window.statusBarColor =
            ContextCompat.getColor(MyApplication.context, R.color.backgroundcolor)
        window.navigationBarColor =
            ContextCompat.getColor(MyApplication.context, R.color.backgroundcolor)
        if (!isDarkTheme(this)) {
            insetsController.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }

        }
    }

    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
}
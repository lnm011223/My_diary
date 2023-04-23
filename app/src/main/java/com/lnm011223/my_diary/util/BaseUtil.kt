package com.lnm011223.my_diary.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.Window
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.base.MyApplication
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

/**

 * @Author liangnuoming
 * @Date 2023/3/25-23:48

 */
object BaseUtil {
    internal fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }



    internal fun rightColor(window: Window, activity: Activity) {
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        window.statusBarColor =
            ContextCompat.getColor(MyApplication.context, R.color.backgroundcolor)
        window.navigationBarColor =
            ContextCompat.getColor(MyApplication.context, R.color.backgroundcolor)
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
        if (!BaseUtil.isDarkTheme(activity)) {

            //insetsController?.isAppearanceLightStatusBars = true
            //insetsController?.isAppearanceLightNavigationBars = true
            insetsController.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true


            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dayOfWeek(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.getDefault())
        val localDate = LocalDate.parse(dateString.substring(0, 8), formatter)
        val dayOfWeek = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return dayOfWeek.replace("星期", "周")
    }
    
    internal fun second2Date(secondNum: Long): String {
        //创建一个 Date 对象并传入时间戳参数
        val date = Date(secondNum)
        // 创建 SimpleDateFormat 对象以定义日期显示格式
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        // 使用 format() 方法将日期转换为指定格式的字符串
        val formattedDate = sdf.format(date)
//        Log.d("millisecond", formattedDate)
        return formattedDate
    }

    internal fun second2Day(secondNum: Long): String {
        //创建一个 Date 对象并传入时间戳参数
        val date = Date(secondNum)
        // 创建 SimpleDateFormat 对象以定义日期显示格式
        val sdf = SimpleDateFormat("yyyy年 MM 月 dd 日 E", Locale.getDefault())
        // 使用 format() 方法将日期转换为指定格式的字符串
        val formattedDate = sdf.format(date)
//        Log.d("millisecond", formattedDate)
        return formattedDate
    }


}


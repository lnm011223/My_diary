package com.lnm011223.my_diary.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
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
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**

 * @Author liangnuoming
 * @Date 2023/3/25-23:48

 */
object BaseUtil {
    // Convert px to dp
    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()

    //Convert dp to px
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    fun View.gone() = run { visibility = View.GONE }

    fun View.visible() = run { visibility = View.VISIBLE }

    fun View.invisible() = run { visibility = View.INVISIBLE }

    infix fun View.visibleIf(condition: Boolean) =
        run { visibility = if (condition) View.VISIBLE else View.GONE }

    infix fun View.goneIf(condition: Boolean) =
        run { visibility = if (condition) View.GONE else View.VISIBLE }

    infix fun View.invisibleIf(condition: Boolean) =
        run { visibility = if (condition) View.INVISIBLE else View.VISIBLE }


    internal fun isDarkTheme(context: Context): Boolean {
//        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//        return flag == Configuration.UI_MODE_NIGHT_YES
        val nightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightMode == Configuration.UI_MODE_NIGHT_YES
                || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

    }



    internal fun rightColor(window: Window, activity: Activity) {
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        window.statusBarColor =
            ContextCompat.getColor(activity, R.color.backgroundcolor)
        window.navigationBarColor =
            ContextCompat.getColor(activity, R.color.backgroundcolor)
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
        if (!isDarkTheme(activity)) {

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


    @RequiresApi(Build.VERSION_CODES.O)
    fun day2Text(dateString: String): String {
        return "${dateString.substring(8..9)}:${dateString.substring(10..11)}"
    }



    internal fun second2Date(secondNum: Long): String {
        //创建一个 Date 对象并传入时间戳参数
        val date = Date(secondNum)
        // 创建 SimpleDateFormat 对象以定义日期显示格式
        val sdf = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())

//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        // 使用 format() 方法将日期转换为指定格式的字符串
        val formattedDate = sdf.format(date)
//        Log.d("millisecond", formattedDate)
        return formattedDate
    }

    internal fun second2Date2(secondNum: Long): String {
        //创建一个 Date 对象并传入时间戳参数
        val date = Date(secondNum)
        // 创建 SimpleDateFormat 对象以定义日期显示格式
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
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


    fun Fragment.shorttoast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.shorttoast(@StringRes message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun Activity.shorttoast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Activity.shorttoast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.longtoast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun Fragment.longtoast(@StringRes message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun Activity.longtoast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun Activity.longtoast(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun Any?.printToLog(tag: String = "DEBUG_LOG") {
        Log.d(tag, toString())
    }


    fun View.snackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, message, duration).show()
    }

    fun View.snackbar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, message, duration).show()
    }
}


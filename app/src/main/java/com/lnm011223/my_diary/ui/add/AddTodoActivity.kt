package com.lnm011223.my_diary.ui.add

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.databinding.ActivityAddTodoBinding
import com.lnm011223.my_diary.util.BaseUtil
import com.xiaofeidev.appreveal.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class AddTodoActivity : BaseActivity() {
    private lateinit var binding: ActivityAddTodoBinding
    private var secondNum: Long = 0

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        BaseUtil.rightColor(window,this)
        binding.dateText.text = SimpleDateFormat("MM 月 dd 日 E").format(Date())
        binding.picker.setOnDateTimeChangedListener {
            millisecond ->
            secondNum = millisecond

        }
        binding.completeButton.setOnClickListener {
            //创建一个 Date 对象并传入时间戳参数
            val date = Date(secondNum)
            // 创建 SimpleDateFormat 对象以定义日期显示格式
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            // 使用 format() 方法将日期转换为指定格式的字符串
            val formattedDate = sdf.format(date)
            Log.d("millisecond", formattedDate)
        }
    }


}
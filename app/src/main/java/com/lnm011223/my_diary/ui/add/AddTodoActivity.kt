package com.lnm011223.my_diary.ui.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.lnm011223.my_diary.databinding.ActivityAddTodoBinding
import com.lnm011223.my_diary.util.BaseUtil
import com.lnm011223.my_diary.base.BaseActivity
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
            Log.d("millisecond", BaseUtil.second2Date(secondNum))
        }
    }


}
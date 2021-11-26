package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.lnm011223.my_diary.databinding.ActivityAddBinding
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dateText.text = SimpleDateFormat("MM 月 dd 日 E").format(Date())
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        window.statusBarColor = Color.parseColor("#F3F3EC")
        window.navigationBarColor = Color.parseColor("#F3F3EC")
        //insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        //insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        if (!isDarkTheme(this)){

            //insetsController?.isAppearanceLightStatusBars = true
            //insetsController?.isAppearanceLightNavigationBars = true
            insetsController?.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true


            }

        }
        binding.completeButton.setOnClickListener {
            val mood = mood_edit.text.toString()
            val diary_text = diarytext_edit.text.toString()
        }
    }
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
}
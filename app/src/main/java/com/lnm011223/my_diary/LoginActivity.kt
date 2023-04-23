package com.lnm011223.my_diary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lnm011223.my_diary.databinding.ActivityLoginBinding
import com.lnm011223.my_diary.databinding.ActivitySplashBinding
import com.lnm011223.my_diary.util.BaseUtil

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var isRight = false
    var isLock = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        BaseUtil.rightColor(window, this)
        if (!isLock) {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        } else {

        }
    }
}
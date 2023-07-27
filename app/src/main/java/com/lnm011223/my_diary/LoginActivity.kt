package com.lnm011223.my_diary

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lnm011223.my_diary.databinding.ActivityLoginBinding
import com.lnm011223.my_diary.databinding.ActivitySplashBinding
import com.lnm011223.my_diary.util.BaseUtil

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        BaseUtil.rightColor(window, this)
        val yiyan = intent.getStringExtra("yiyan")
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        val isEmpty = prefs?.getString("username",null)
        if (!isEmpty.isNullOrEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("yiyan",yiyan)
            startActivity(intent)
            finish()
        } else {
            binding.completeButton.setOnClickListener {
                val username = binding.usernameEdit.text.toString()
                val editor = getSharedPreferences("data", Context.MODE_PRIVATE)?.edit()
                editor?.putString("username", username)
                editor?.apply()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("yiyan",yiyan)
                startActivity(intent)
                finish()
            }

        }
    }
}
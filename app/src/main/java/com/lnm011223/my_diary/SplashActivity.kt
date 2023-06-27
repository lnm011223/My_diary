package com.lnm011223.my_diary


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lnm011223.my_diary.databinding.ActivitySplashBinding
import com.lnm011223.my_diary.logic.network.YiyanService
import retrofit2.*
import retrofit2.converter.scalars.ScalarsConverterFactory
import androidx.lifecycle.lifecycleScope
import com.lnm011223.my_diary.util.BaseUtil
import kotlinx.coroutines.delay


class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    var onlineFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        BaseUtil.rightColor(window, this)
        val intent = Intent(this, MainActivity::class.java)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://v1.hitokoto.cn")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val yiyanService = retrofit.create(YiyanService::class.java)
        yiyanService.getYiyanData("d", "text").enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val yiyan = response.body()
                Log.d("yiyan", yiyan.toString())
                if (yiyan != null) {
                    onlineFlag = true
                    binding.splashText.text = yiyan
                    intent.putExtra("yiyan",yiyan)
                }


            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
            }
        })

        lifecycleScope.launchWhenCreated {
            if (!onlineFlag) {
                binding.splashText.text = "欢迎回来！"
            }
            delay(2000L) // 延迟 2 秒钟
            startActivity(intent)
            finish()
        }


    }
}




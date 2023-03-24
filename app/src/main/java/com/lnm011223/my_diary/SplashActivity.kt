package com.lnm011223.my_diary

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.databinding.ActivityMainBinding
import com.lnm011223.my_diary.databinding.ActivitySplashBinding
import com.lnm011223.my_diary.logic.network.YiyanService
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        window.statusBarColor = ContextCompat.getColor(MyApplication.context, R.color.backgroundcolor)
        window.navigationBarColor = ContextCompat.getColor(MyApplication.context, R.color.backgroundcolor)
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
        if (!isDarkTheme(this)) {

            //insetsController?.isAppearanceLightStatusBars = true
            //insetsController?.isAppearanceLightNavigationBars = true
            insetsController.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true


            }

        }
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
                    binding.splashText.text = yiyan

                }


            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
            }
        })
        lifecycleScope.launchWhenCreated {
            delay(2000L) // 延迟 2 秒钟
            startActivity(intent)
            finish()
        }
//        installSplashScreen()
        /*跳转页面*/


    }
}
private fun isDarkTheme(context: Context): Boolean {
    val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return flag == Configuration.UI_MODE_NIGHT_YES
}
package com.lnm011223.my_diary.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

import com.google.android.material.color.DynamicColors

class MyApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        // apply dynamic color
        DynamicColors.applyToActivitiesIfAvailable(this)
        // 打开darkmode
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // 关闭darkmode
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // darkmode跟随系统
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    }


}
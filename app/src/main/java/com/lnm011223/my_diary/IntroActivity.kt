package com.lnm011223.my_diary

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper

class IntroActivity : AppIntro2() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
        dbHelper.writableDatabase
        setProgressIndicator()
        setImmersiveMode()
        isColorTransitionsEnabled = true
        setIndicatorColor(
            selectedIndicatorColor = getColor(R.color.main),
            unselectedIndicatorColor = getColor(R.color.main)
        )
        setTransformer(AppIntroPageTransformerType.Depth)
        addSlide(
            AppIntroFragment.createInstance(
                title = "The title of your slide",
                description = "A description that will be shown on the bottom",
                imageDrawable = R.drawable.todo1,
                titleColorRes = R.color.titletextcolor,
                descriptionColorRes = R.color.red,
                backgroundColorRes = R.color.backgroundcolor,

        ))
        addSlide(AppIntroFragment.createInstance(
            title = "...Let's get started!",
            description = "This is the last slide, I won't annoy you more :)",
            imageDrawable = R.drawable.mood_2,
            titleColorRes = R.color.titletextcolor,
            descriptionColorRes = R.color.red,
            backgroundColorRes = R.color.backgroundcolor,
        ))
    }
    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
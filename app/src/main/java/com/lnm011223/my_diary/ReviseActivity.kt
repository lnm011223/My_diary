package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lnm011223.my_diary.MyApplication.Companion.context

import com.lnm011223.my_diary.databinding.ActivityReviseBinding
import java.text.SimpleDateFormat
import java.util.*

class ReviseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviseBinding

    var mood_flag:Int = 1
    var uri1: String = ""
    @SuppressLint("SimpleDateFormat")
    val formAlbum = 2

    var flag1 = false
    var flag2 = false
    var flag3 = false
    var flag4 = false
    var flag5 = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)
        val db = dbHelper.writableDatabase
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        window.statusBarColor = ContextCompat.getColor(context,R.color.backgroundcolor)
        window.navigationBarColor = ContextCompat.getColor(context,R.color.backgroundcolor)
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        if (!isDarkTheme(this)){


            insetsController?.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true



            }

        }
        val datetext = intent.getStringExtra("datetext")
        var diarytext = intent.getStringExtra("diarytext")
        var imageuri = intent.getStringExtra("imageuri")?.toUri()
        val position = intent.getStringExtra("position")
        Log.d("livedata",position.toString())
        var diary = intent.getParcelableExtra<Diary>("diary") as Diary
        Log.d("aaa",diary.moon.toString())
        mood_flag = diary.moon
        uri1 = diary.diary_image
        diarytext = diary.diary_text

        when (diary.moon) {
            R.drawable.mood_1 -> binding.mood1Image.setImageResource(R.drawable.mood_1)
            R.drawable.mood_2 -> binding.mood2Image.setImageResource(R.drawable.mood_2)
            R.drawable.mood_3 -> binding.mood3Image.setImageResource(R.drawable.mood_3)
            R.drawable.mood_4 -> binding.mood4Image.setImageResource(R.drawable.mood_4)
            R.drawable.mood_5 -> binding.mood5Image.setImageResource(R.drawable.mood_5)
        }

        binding.dateText.text = datetext
        binding.diarytextEdit.setText(diarytext)
        binding.imageShow.setImageURI(imageuri)
        val diarytext_backup = intent.getStringExtra("diarytext")

        binding.completeButton.setOnClickListener {
            diarytext = binding.diarytextEdit.text.toString()

            val diary_value = ContentValues().apply {
                put("imageuri",uri1)
                put("moodid",mood_flag)

                put("diarytext",diarytext)
            }
            db.update("diarydata",diary_value,"id = ?", arrayOf(diary.id.toString()))
            val intentResult = Intent()
            val reviseItem = Diary(diary.id,diary.date_text,mood_flag,uri1,binding.diarytextEdit.text.toString())
            intentResult.apply {
                putExtra("position",position)

                putExtra("reviseDiary",reviseItem)
            }

            Log.d("livedata",reviseItem.toString())
            setResult(RESULT_OK,intentResult)
            onBackPressed()

        }
        binding.imageShow.setOnClickListener {

            //val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            //intent.addCategory(Intent.CATEGORY_OPENABLE)
            //intent.type = "image/*"
            //startActivityForResult(intent,formAlbum)
            val intent = Intent(Intent.ACTION_PICK) // 打开相册


            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")



            startActivityForResult(intent, formAlbum)
        }
        binding.mood1Image.setOnClickListener {
            if (flag1 == false) {
                binding.mood1Image.setImageResource(R.drawable.mood_1)
                flag1 = true
                changeOther(binding.mood1Image,flag1)
                mood_flag = R.drawable.mood_1
            }else{
                binding.mood1Image.setImageResource(R.drawable.mood_1_last)
                flag1 = false
            }
        }
        binding.mood2Image.setOnClickListener {
            if (flag2 == false) {
                binding.mood2Image.setImageResource(R.drawable.mood_2)
                flag2 = true
                changeOther(binding.mood2Image,flag2)
                mood_flag = R.drawable.mood_2
            }else{
                binding.mood2Image.setImageResource(R.drawable.mood_2_last)
                flag2 = false
            }
        }
        binding.mood3Image.setOnClickListener {
            if (flag3 == false) {
                binding.mood3Image.setImageResource(R.drawable.mood_3)
                flag3 = true
                changeOther(binding.mood3Image,flag3)
                mood_flag = R.drawable.mood_3
            }else{
                binding.mood3Image.setImageResource(R.drawable.mood_3_last)
                flag3 = false
            }
        }
        binding.mood4Image.setOnClickListener {
            if (flag4 == false) {
                binding.mood4Image.setImageResource(R.drawable.mood_4)
                flag4 = true
                changeOther(binding.mood4Image,flag4)
                mood_flag = R.drawable.mood_4
            }else{
                binding.mood4Image.setImageResource(R.drawable.mood_4_last)
                flag4 = false
            }
        }
        binding.mood5Image.setOnClickListener {
            if (flag5 == false) {
                binding.mood5Image.setImageResource(R.drawable.mood_5)
                flag5 = true
                changeOther(binding.mood5Image,flag5)
                mood_flag = R.drawable.mood_5
            }else{
                binding.mood5Image.setImageResource(R.drawable.mood_5_last)
                flag5 = false
            }
        }

    }
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
    private fun changeOther(imageView: ImageView, flag:Boolean){

        if (flag==true){
            when (imageView) {
                binding.mood1Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag4 = false
                    flag5 = false

                }
                binding.mood2Image -> {
                    binding.apply {
                        mood1Image.setImageResource(R.drawable.mood_1_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag1 = false
                    flag3 = false
                    flag4 = false
                    flag5 = false

                }
                binding.mood3Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood1Image.setImageResource(R.drawable.mood_1_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag1 = false
                    flag4 = false
                    flag5 = false

                }
                binding.mood4Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood1Image.setImageResource(R.drawable.mood_1_last)
                        mood5Image.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag1 = false
                    flag5 = false

                }
                binding.mood5Image -> {
                    binding.apply {
                        mood2Image.setImageResource(R.drawable.mood_2_last)
                        mood3Image.setImageResource(R.drawable.mood_3_last)
                        mood4Image.setImageResource(R.drawable.mood_4_last)
                        mood1Image.setImageResource(R.drawable.mood_1_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag4 = false
                    flag1 = false

                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            formAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        uri1 = UriUtils.getFilePathFromURI(MyApplication.context,uri).toString()

                        binding.imageShow.setImageURI(uri)
                        binding.imageShow.setPadding(0,0,0,0)


                    }
                }
            }
        }
    }
}
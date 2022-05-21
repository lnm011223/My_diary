package com.lnm011223.my_diary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.lnm011223.my_diary.databinding.ActivityAddBinding

import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    var mood_flag:Int = R.drawable.mood_1
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
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dateText.text = SimpleDateFormat("MM 月 dd 日 E").format(Date())
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        window.statusBarColor = Color.parseColor("#F3F3EC")
        window.navigationBarColor = Color.parseColor("#F3F3EC")
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        if (!isDarkTheme(this)){

            //insetsController?.isAppearanceLightStatusBars = true
            //insetsController?.isAppearanceLightNavigationBars = true
            insetsController?.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true


            }

        }
        val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)
        dbHelper.writableDatabase

        binding.imageShow.setOnClickListener {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent,formAlbum)
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
        binding.completeButton.setOnClickListener {

            val diary_text = binding.diarytextEdit.text.toString()


            val intent = Intent()
            val db = dbHelper.writableDatabase
            val diary_value = ContentValues().apply {
                put("imageuri",uri1)
                put("moodid",mood_flag)
                put("datetext",SimpleDateFormat("MM 月 dd 日 E").format(Date()))
                put("diarytext",diary_text)
            }
            db.insert("diarydata",null,diary_value)

            intent.apply {
                putExtra("image_uri",uri1)
                putExtra("mood_id",mood_flag)
                putExtra("date_text",SimpleDateFormat("dd E").format(Date()))
                putExtra("diary_text1",diary_text)

            }

            setResult(RESULT_OK,intent)
            finish()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            formAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        uri1 = UriUtils.getFilePathFromURI(MyApplication.context,uri).toString()
                        //val bitmap = getBitmapFromuri(uri)
                        //val path = UriUtils.getFilePathFromURI(MyApplication.context,uri)
                        binding.imageShow.setImageURI(uri)
                        binding.imageShow.setPadding(0,0,0,0)


                    }
                }
            }
        }
    }
    private fun getBitmapFromuri(uri: Uri) = contentResolver.openFileDescriptor(uri,"r")?.use { BitmapFactory.decodeFileDescriptor(it.fileDescriptor) }
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
    private fun changeOther(imageView: ImageView,flag:Boolean){

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
}
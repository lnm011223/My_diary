package com.lnm011223.my_diary.ui.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.*
import com.lnm011223.my_diary.base.MyApplication.Companion.context
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.databinding.ActivityAddBinding
import com.lnm011223.my_diary.logic.model.Diary
import com.lnm011223.my_diary.util.BaseUtil
import com.lnm011223.my_diary.util.DensityUtil
import com.lnm011223.my_diary.util.UriUtils
import com.xiaofeidev.appreveal.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*


class AddDiaryActivity : BaseActivity() {
    private lateinit var binding: ActivityAddBinding
    var mood_flag: Int = R.drawable.mood_1
    var uri1: String = ""
    val moodMap = mapOf(
        R.drawable.mood_1 to 1,
        R.drawable.mood_2 to 2,
        R.drawable.mood_3 to 3,
        R.drawable.mood_4 to 4,
        R.drawable.mood_5 to 5,
    )

    @SuppressLint("SimpleDateFormat")
    val formAlbum = 2

    var flag1 = false
    var flag2 = false
    var flag3 = false
    var flag4 = false
    var flag5 = false

    @SuppressLint("SimpleDateFormat", "Recycle", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dateText.text = SimpleDateFormat("MM 月 dd 日 E").format(Date())
        BaseUtil.rightColor(window, this)
        val dbHelper = MyDatabaseHelper(context, "DiaryData.db", 1)
        dbHelper.writableDatabase
        binding.imageShow.setPadding(DensityUtil.dip2px(context, 60f))
        binding.imageShow.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) // 打开相册
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, formAlbum)
        }
        binding.mood1Image.setOnClickListener {
            if (!flag1) {
                binding.mood1Image.setImageResource(R.drawable.mood_1)
                flag1 = true
                changeOther(binding.mood1Image, flag1)
                mood_flag = R.drawable.mood_1
            } else {
                binding.mood1Image.setImageResource(R.drawable.mood_1_last)
                flag1 = false
            }
        }
        binding.mood2Image.setOnClickListener {
            if (!flag2) {
                binding.mood2Image.setImageResource(R.drawable.mood_2)
                flag2 = true
                changeOther(binding.mood2Image, flag2)
                mood_flag = R.drawable.mood_2
            } else {
                binding.mood2Image.setImageResource(R.drawable.mood_2_last)
                flag2 = false
            }
        }
        binding.mood3Image.setOnClickListener {
            if (!flag3) {
                binding.mood3Image.setImageResource(R.drawable.mood_3)
                flag3 = true
                changeOther(binding.mood3Image, flag3)
                mood_flag = R.drawable.mood_3
            } else {
                binding.mood3Image.setImageResource(R.drawable.mood_3_last)
                flag3 = false
            }
        }
        binding.mood4Image.setOnClickListener {
            if (!flag4) {
                binding.mood4Image.setImageResource(R.drawable.mood_4)
                flag4 = true
                changeOther(binding.mood4Image, flag4)
                mood_flag = R.drawable.mood_4
            } else {
                binding.mood4Image.setImageResource(R.drawable.mood_4_last)
                flag4 = false
            }
        }
        binding.mood5Image.setOnClickListener {
            if (!flag5) {
                binding.mood5Image.setImageResource(R.drawable.mood_5)
                flag5 = true
                changeOther(binding.mood5Image, flag5)
                mood_flag = R.drawable.mood_5
            } else {
                binding.mood5Image.setImageResource(R.drawable.mood_5_last)
                flag5 = false
            }
        }
        binding.completeButton.doOnAttach { }
        binding.completeButton.setOnClickListener { view ->

            val diary_text = binding.diarytextEdit.text.toString()

            val datetext: String? = SimpleDateFormat("MM 月 dd 日 E").format(Date())
            val intent = Intent()
            val db = dbHelper.writableDatabase
            val diary_value = ContentValues().apply {
                put("imageuri", uri1)
                put("moodid", moodMap[mood_flag])
                put("datetext", datetext)
                put("diarytext", diary_text)
            }
            val success = db.insert("diarydata", null, diary_value)
            Log.d("successtest", success.toString())
            val id = success.toString().toInt()

            //直接返回数据库里的最后一行数据的id，即新添加的id
//            val cursor = db.rawQuery("select * from diarydata ", null)
//            if (cursor.moveToLast()) {
//
//                id = cursor.getString(cursor.getColumnIndex("id")).toInt()
//                datetext = cursor.getString(cursor.getColumnIndex("datetext"))
//
//            }
//            cursor.close()

            val location = IntArray(2)
            view.getLocationInWindow(location)
            //把点击按钮的中心位置坐标传过去作为 AddDiaryActivity 的揭露动画圆心
            intent.putExtra(CLICK_X, location[0] + view.width / 2)
            intent.putExtra(CLICK_Y, location[1] + view.height / 2)

            if (id != -1) {
                Log.d("successtest", success.toString())
                intent.putExtra(
                    "addDiary", Diary(
                        id, datetext!!,
                        moodMap[mood_flag]!!, uri1, diary_text
                    )
                )
                setResult(RESULT_OK, intent)
                super.onBackPressed()
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("提醒：")
                    setMessage("您今天已创建过日记，如有需要请修改或删除该日记。")

                    setPositiveButton("是") { _, _ ->
                        super.onBackPressed()

                    }


                    show()
                }
            }


            //finish()

        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            formAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        uri1 = UriUtils.getFilePathFromURI(context, uri).toString()
                        binding.imageShow.scaleType = ImageView.ScaleType.FIT_XY
                        binding.imageShow.setImageURI(uri1.toUri())
                        binding.imageShow.setPadding(0, 0, 0, 0)


                    }
                }
            }
        }
    }


    private fun changeOther(imageView: ImageView, flag: Boolean) {

        if (flag == true) {
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
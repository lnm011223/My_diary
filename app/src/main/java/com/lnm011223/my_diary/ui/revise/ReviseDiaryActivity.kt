package com.lnm011223.my_diary.ui.revise

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.setPadding
import com.lnm011223.my_diary.base.MyApplication.Companion.context
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.R

import com.lnm011223.my_diary.databinding.ActivityReviseDairyBinding
import com.lnm011223.my_diary.logic.model.Diary
import com.lnm011223.my_diary.util.BaseUtil

import com.lnm011223.my_diary.util.DensityUtil
import com.lnm011223.my_diary.util.UriUtils

class ReviseDiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviseDairyBinding
    var mood_flag: Int = 1
    var uri1: String = ""

    @SuppressLint("SimpleDateFormat")
    val formAlbum = 2
    var flag1 = false
    var flag2 = false
    var flag3 = false
    var flag4 = false
    var flag5 = false
    var imageflag = false
    private val moodMap = mapOf(
        R.drawable.mood_1 to 1,
        R.drawable.mood_2 to 2,
        R.drawable.mood_3 to 3,
        R.drawable.mood_4 to 4,
        R.drawable.mood_5 to 5,
    )
    private val moodMapR = mapOf(
        1 to R.drawable.mood_1,
        2 to R.drawable.mood_2,
        3 to R.drawable.mood_3,
        4 to R.drawable.mood_4,
        5 to R.drawable.mood_5,
    )

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviseDairyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dbHelper = MyDatabaseHelper(context, "DiaryData.db", 1)
        val db = dbHelper.writableDatabase
        BaseUtil.rightColor(window, this)
        val datetext = intent.getStringExtra("datetext")
        var diarytext: String?
        val imageuri = intent.getStringExtra("imageuri")?.toUri()
        val position = intent.getStringExtra("position")
        Log.d("livedata", position.toString())
        val diary = intent.getParcelableExtra<Diary>("diary") as Diary
        Log.d("aaa", diary.moon.toString())
        mood_flag = moodMapR[diary.moon]!!
        uri1 = diary.diary_image
        diarytext = diary.diary_text
        //设置初始展示效果
        when (moodMapR[diary.moon]) {
            R.drawable.mood_1 -> binding.mood1Image.setImageResource(R.drawable.mood_1)
            R.drawable.mood_2 -> binding.mood2Image.setImageResource(R.drawable.mood_2)
            R.drawable.mood_3 -> binding.mood3Image.setImageResource(R.drawable.mood_3)
            R.drawable.mood_4 -> binding.mood4Image.setImageResource(R.drawable.mood_4)
            R.drawable.mood_5 -> binding.mood5Image.setImageResource(R.drawable.mood_5)
        }
        binding.dateText.text = "${datetext?.substring(4..5)}月 ${datetext?.substring(6..7)}日 ${BaseUtil.dayOfWeek(
            datetext.toString()
        )}"
        binding.diarytextEdit.setText(diarytext)

        if (imageuri?.toString() != "" && !imageflag) {
            binding.imageShow.scaleType = ImageView.ScaleType.FIT_XY
            binding.imageShow.setImageURI(imageuri)
        } else {
            binding.imageShow.setPadding(DensityUtil.dip2px(context, 60f))
        }


        binding.completeButton.setOnClickListener {
            diarytext = binding.diarytextEdit.text.toString()
            val diary_value = ContentValues().apply {
                put("imageuri", uri1)
                put("moodid", moodMap[mood_flag])
                put("diarytext", diarytext)
            }
            db.update("diarydata", diary_value, "id = ?", arrayOf(diary.id.toString()))
            val intentResult = Intent()
            val reviseItem = Diary(
                diary.id,
                diary.date_text,
                moodMap[mood_flag]!!,
                uri1,
                binding.diarytextEdit.text.toString()
            )
            intentResult.apply {
                putExtra("position", position)
                putExtra("reviseDiary", reviseItem)
            }
            setResult(RESULT_OK, intentResult)
            onBackPressed()

        }
        binding.imageShow.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK) // 打开相册
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, formAlbum)
        }

        var imageViewList = listOf(
            binding.mood1Image,
            binding.mood2Image,
            binding.mood3Image,
            binding.mood4Image,
            binding.mood5Image
        )
        var flagList = mutableListOf(flag1, flag2, flag3, flag4, flag5)
        val moodIdList = listOf(
            R.drawable.mood_1_last,
            R.drawable.mood_2_last,
            R.drawable.mood_3_last,
            R.drawable.mood_4_last,
            R.drawable.mood_5_last
        )

        binding.mood1Image.setOnClickListener {
            flag1 = moodClick(
                flag1,
                binding.mood1Image,
                R.drawable.mood_1,
                R.drawable.mood_1_last,
                imageViewList,
                flagList,
                moodIdList
            )
        }
        binding.mood2Image.setOnClickListener {
            flag2 = moodClick(
                flag2,
                binding.mood2Image,
                R.drawable.mood_2,
                R.drawable.mood_2_last,
                imageViewList,
                flagList,
                moodIdList
            )
        }
        binding.mood3Image.setOnClickListener {
            flag3 = moodClick(
                flag3,
                binding.mood3Image,
                R.drawable.mood_3,
                R.drawable.mood_3_last,
                imageViewList,
                flagList,
                moodIdList
            )
        }
        binding.mood4Image.setOnClickListener {
            flag4 = moodClick(
                flag4,
                binding.mood4Image,
                R.drawable.mood_4,
                R.drawable.mood_4_last,
                imageViewList,
                flagList,
                moodIdList
            )
        }
        binding.mood5Image.setOnClickListener {
            flag5 = moodClick(
                flag5,
                binding.mood5Image,
                R.drawable.mood_5,
                R.drawable.mood_5_last,
                imageViewList,
                flagList,
                moodIdList
            )
        }

    }

    private fun moodClick(
        flag: Boolean,
        moodImage: ImageView,
        mood: Int,
        mood_last: Int,
        imageViewList: List<ImageView>,
        flagList: MutableList<Boolean>,
        moodIdList: List<Int>
    ): Boolean {
        return if (!flag) {
            moodImage.setImageResource(mood)

            changeOther(moodImage, !flag, imageViewList, flagList, moodIdList)
            mood_flag = mood
            !flag
        } else {
            moodImage.setImageResource(mood_last)
            !flag
        }
    }


    //切换mood选择效果
    private fun changeOther(
        imageView: ImageView,
        flag: Boolean,
        imageViewList: List<ImageView>,
        flagList: MutableList<Boolean>,
        moodIdList: List<Int>
    ) {

        for (i in imageViewList) {
            if (i == imageView) {
                continue
            }
            i.setImageResource(moodIdList[imageViewList.indexOf(i)])
            flagList[imageViewList.indexOf(i)] = false
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

}
package com.lnm011223.my_diary.ui.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.view.*
import com.lnm011223.my_diary.base.MyApplication.Companion.context
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.R

import com.lnm011223.my_diary.logic.model.Diary
import com.lnm011223.my_diary.util.BaseUtil
import com.lnm011223.my_diary.util.DensityUtil
import com.lnm011223.my_diary.util.UriUtils
import com.lnm011223.my_diary.base.BaseActivity
import com.lnm011223.my_diary.databinding.ActivityAddDiaryBinding
import com.lnm011223.my_diary.util.BaseUtil.px
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import java.text.SimpleDateFormat
import java.util.*


class AddDiaryActivity : BaseActivity() {
    private lateinit var binding: ActivityAddDiaryBinding
    private lateinit var selectDate: String
    private lateinit var dateString: String
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

    @SuppressLint("SimpleDateFormat", "Recycle", "Range", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.dateText.text = SimpleDateFormat("MM 月 dd 日 E").format(Date())
        selectDate = BaseUtil.second2Day(System.currentTimeMillis())
        dateString = BaseUtil.second2Date2(System.currentTimeMillis())
        BaseUtil.rightColor(window, this)
        val dbHelper = MyDatabaseHelper(context, "DiaryData.db", 1)
        dbHelper.writableDatabase
        binding.imageShow.setPadding(60.px)
        binding.imageShow.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK) // 打开相册
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, formAlbum)
//            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, formAlbum)
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
        binding.completeButton.doOnAttach { }
        binding.daySelect.setOnClickListener { view ->
            CardDatePickerDialog.builder(view.context)

                .setTitle("请选择日期：")
                .showBackNow(false)
                .setBackGroundModel(R.drawable.shape_sheet_dialog_bg)
                .setDisplayType(DateTimeConfig.YEAR, DateTimeConfig.MONTH, DateTimeConfig.DAY)
                .showFocusDateInfo(false)
                .setMaxTime(System.currentTimeMillis())
                .setPickerLayout(R.layout.layout_day_picker_segmentation)
                .setThemeColor(Color.parseColor("#3EB06A"))
                .setAssistColor(
                    if (BaseUtil.isDarkTheme(view.context)) Color.parseColor("#707070") else Color.parseColor(
                        "#b9b9b9"
                    )
                )
                .setOnChoose { millisecond ->
                    selectDate = BaseUtil.second2Day(millisecond)
                    dateString = BaseUtil.second2Date2(millisecond)
                    binding.dateText.text = selectDate.substring(6..17)

                }.build().show()
        }
        binding.completeButton.setOnClickListener { view ->

            val diary_text = binding.diarytextEdit.text.toString()

            val datetext: String? = SimpleDateFormat("yyyy年 MM 月 dd 日 E").format(Date())
            val intent = Intent()
            val db = dbHelper.writableDatabase
            val diary_value = ContentValues().apply {
                put("imageuri", uri1)
                put("moodid", moodMap[mood_flag])
                put("datetext", dateString)
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
                        id, dateString,
                        moodMap[mood_flag]!!, uri1, diary_text
                    )
                )
                setResult(RESULT_OK, intent)
                super.onBackPressed()
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("提醒：")
                    setMessage("您今天已创建过日记，如有需要请修改日期或删除该日记。")

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

}




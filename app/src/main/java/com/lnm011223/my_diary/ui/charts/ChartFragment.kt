package com.lnm011223.my_diary.ui.charts

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan

import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.*
import com.lnm011223.my_diary.MainViewModel
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.databinding.FragmentChartBinding
import com.lnm011223.my_diary.logic.model.Daymood
import com.lnm011223.my_diary.util.BaseUtil
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class ChartFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!
    val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
    val moodList = ArrayList<Daymood>()
    var chartlist = listOf<FloatEntry>().toMutableList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root


    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = BaseUtil.second2Date(System.currentTimeMillis())
        binding.monthText.text = "${date.substring(0..3)}年  ${date.substring(5..6)}月 "


        binding.monthSelect.setOnClickListener { view ->
            CardDatePickerDialog.builder(view.context)

                .setTitle("请选择月份：").showBackNow(false)
                .setMaxTime(System.currentTimeMillis())
                .setBackGroundModel(R.drawable.shape_sheet_dialog_bg)
                .setDisplayType(DateTimeConfig.YEAR, DateTimeConfig.MONTH).showFocusDateInfo(false)
                .setPickerLayout(R.layout.layout_month_picker_segmentation)
                .setThemeColor(Color.parseColor("#3EB06A")).setAssistColor(
                    if (BaseUtil.isDarkTheme(view.context)) Color.parseColor("#707070") else Color.parseColor(
                        "#b9b9b9"
                    )
                ).setOnChoose { millisecond ->
                    val selectDate = BaseUtil.second2Date(millisecond)
                    binding.monthText.text =
                        "${selectDate.substring(0..3)}年  ${selectDate.substring(5..6)}月 "
                    initChart(binding.monthText.text.toString())


                }.build().show()
        }
        initChart(binding.monthText.text.toString())

    }

    @SuppressLint("Range")
    fun initChart(date: String) {
        thread {

            moodList.clear()
            val moodNum = arrayListOf<Int>(0, 0, 0, 0, 0, 0)
            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("select * from diarydata ", null)
            val dateSelect = date.substring(0..5) + date.substring(7..8)

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex("id")).toInt()
                    val datetext = cursor.getString(cursor.getColumnIndex("datetext"))
                    val moodid = cursor.getInt(cursor.getColumnIndex("moodid"))
                    if (datetext.substring(0..7) == dateSelect) {
                        val dateNum = datetext.substring(11..12).toInt()

                        moodList.add(Daymood(dateNum, moodid))
                        moodNum[moodid]++

                    }


                } while (cursor.moveToNext())
            }

            cursor.close()
            moodList.sortBy { it.day }
            mainViewModel.moodList.value?.clear()
            mainViewModel.setAllmood(moodList)
            chartlist.removeAll { it.x.toString().isNotEmpty() }
            val numlist = listOf<FloatEntry>().toMutableList()

            for (i in moodList) {
                when (i.mood) {
                    1 -> i.mood = 5
                    2 -> i.mood = 4
                    3 -> i.mood = 3
                    4 -> i.mood = 2
                    5 -> i.mood = 1
                }
                Log.d("mood", i.day.toString())
                chartlist.add(FloatEntry(i.day.toFloat(), i.mood.toFloat()))

            }
            for (i in 1..5) {
                numlist.add(FloatEntry(i.toFloat(), moodNum[i].toFloat()))
            }
            var doneNum = 0
            var ontimeNum = 0
            var overtimeNum = 0
            val cursor1 = db.rawQuery("select * from tododata ", null)
            if (cursor1.moveToFirst()) {
                do {
                    val deadline = cursor1.getString(cursor1.getColumnIndex("deadline"))
                    val isDone = cursor1.getInt(cursor1.getColumnIndex("isDone"))
                    val enddate = cursor1.getString(cursor1.getColumnIndex("enddate"))
                    if (enddate != "0") {
                        val compare = "${enddate.substring(0..3)}年 ${enddate.substring(5..6)}"
                        Log.d("compare", compare + "   " + dateSelect)
                        if ((compare == dateSelect) && (isDone == 1)) {
                            val deadlinenum = deadline.filter { it.isDigit() }.toBigInteger()
                            val enddatenum = enddate.filter { it.isDigit() }.toBigInteger()
                            doneNum++
                            if (enddatenum - deadlinenum > "0".toBigInteger()) {
                                overtimeNum++
                            } else {
                                ontimeNum++
                            }

                        }
                    }

                } while (cursor1.moveToNext())
            }

            cursor1.close()
            val showtext =
                "本月共有${moodList.size}次日记记录，其中开心${moodNum[1]}天,愉悦${moodNum[2]}天,平静${moodNum[3]}天,失落${moodNum[4]}天,伤心${moodNum[5]}天。\n" +
                        "且完成待办事项${doneNum}个，其中按时完成${ontimeNum}个，逾期完成${overtimeNum}个。"
            val spannableString = SpannableString(showtext)
            val pattern = Regex("\\d+")
            val matcher = pattern.findAll(spannableString)

            matcher.forEach {
                val start = it.range.first
                val end = it.range.last + 1

                // 将数字部分的文本颜色设置为绿色并添加下划线和加粗
                val colorSpan = ForegroundColorSpan(Color.parseColor("#3EB06A"))
                val underlineSpan = UnderlineSpan()
                val boldSpan = StyleSpan(Typeface.BOLD)
                spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(underlineSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(boldSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            activity?.runOnUiThread {
                binding.chartView.entryProducer = ChartEntryModelProducer(chartlist)
                binding.chartView1.entryProducer = ChartEntryModelProducer(numlist)
                binding.showText.text = spannableString
            }


        }

    }


}
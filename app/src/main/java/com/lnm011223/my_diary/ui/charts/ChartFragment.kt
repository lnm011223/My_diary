package com.lnm011223.my_diary.ui.charts

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
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
import com.lnm011223.my_diary.logic.model.Diary
import com.lnm011223.my_diary.util.BaseUtil
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.marker.Marker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.random.Random


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
            val moodNum = arrayListOf<Int>(0,0,0,0,0,0)
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
            chartlist.removeAll{ it.x.toString().isNotEmpty() }
            var numlist = listOf<FloatEntry>().toMutableList()

            for (i in moodList) {
                Log.d("mood", i.day.toString())
                chartlist.add(FloatEntry(i.day.toFloat(),i.mood.toFloat()))

            }
            for (i in 1..5) {
                numlist.add(FloatEntry(i.toFloat(),moodNum[i].toFloat()))
            }
            activity?.runOnUiThread {
                binding.chartView.entryProducer = ChartEntryModelProducer(chartlist)
                binding.chartView1.entryProducer = ChartEntryModelProducer(numlist)
            }


        }

    }


}
package com.lnm011223.my_diary.ui.charts

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.lnm011223.my_diary.MainViewModel
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.databinding.FragmentChartBinding
import com.lnm011223.my_diary.databinding.FragmentDashboardBinding
import com.lnm011223.my_diary.util.BaseUtil
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog


class ChartFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root


    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = BaseUtil.second2Date(System.currentTimeMillis())
        binding.monthText.text = "${date.substring(0..3)}年 ${date.substring(5..6)}月"
        binding.monthText.setOnClickListener { view ->
            CardDatePickerDialog.builder(view.context)

                .setTitle("请选择月份：")
                .showBackNow(false)
                .setBackGroundModel(R.drawable.shape_sheet_dialog_bg)
                .setDisplayType(DateTimeConfig.YEAR, DateTimeConfig.MONTH)
                .showFocusDateInfo(false)
                .setPickerLayout(R.layout.layout_month_picker_segmentation)
                .setThemeColor(Color.parseColor("#3EB06A"))
                .setAssistColor(
                    if (BaseUtil.isDarkTheme(view.context)) Color.parseColor("#707070") else Color.parseColor(
                        "#b9b9b9"
                    )
                )
                .setOnChoose { millisecond ->
                    val selectDate = BaseUtil.second2Date(millisecond)
                    binding.monthText.text = "${selectDate.substring(0..3)}年 ${selectDate.substring(5..6)}月"
                }.build().show()
        }
    }


}
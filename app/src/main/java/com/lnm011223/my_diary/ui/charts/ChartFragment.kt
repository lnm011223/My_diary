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

    }


}
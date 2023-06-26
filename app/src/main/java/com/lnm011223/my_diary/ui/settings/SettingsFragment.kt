package com.lnm011223.my_diary.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.databinding.FragmentSettingsBinding
import com.lnm011223.my_diary.util.BaseUtil
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import kotlin.random.Random

// TODO: 添加深色模式开关按钮
// TODO: 可以考虑适配material3样式
class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("CommitPrefEdits", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = activity?.getSharedPreferences("data", Context.MODE_PRIVATE)
        val isNotice = prefs?.getBoolean("isNotice", false)
        val welcometext = prefs?.getString("welcometext","天天开心！")
        binding.isNoticeButton.isChecked = isNotice!!
        binding.welcomeText.text = welcometext
        when (isNotice) {
            true -> {
                binding.timecard.isVisible = true
                val hour = prefs.getInt("hour", 22)
                val min = prefs.getInt("minute", 30)
                binding.noticeTimeText.text = "$hour:$min"
            }
            false -> {
                binding.timecard.isVisible = false
            }
        }

        binding.avatar.setOnClickListener {
            val moodMap = mapOf(
                1 to R.drawable.mood_1,
                2 to R.drawable.mood_2,
                3 to R.drawable.mood_3,
                4 to R.drawable.mood_4,
                5 to R.drawable.mood_5,
            )
            val mood_num = (1 .. 5).random()
            binding.avatar.setImageResource(moodMap[mood_num]!!)
        }

        binding.isNoticeButton.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    binding.timecard.isVisible = true
                    val editor =
                        activity?.getSharedPreferences("data", Context.MODE_PRIVATE)?.edit()
                    editor?.putBoolean("isNotice", true)
                    editor?.apply()
                }

                false -> {
                    binding.timecard.isVisible = false
                    val editor =
                        activity?.getSharedPreferences("data", Context.MODE_PRIVATE)?.edit()
                    editor?.putBoolean("isNotice", false)
                    editor?.apply()
                }
            }

        }

        binding.changeUsernameBtn.setOnClickListener {
            // 设置布局
            val view = layoutInflater.inflate(R.layout.username_dialog_layout, null)
            val input = view.findViewById<EditText>(R.id.input_text)

            AlertDialog.Builder(requireActivity()).apply {

                setTitle("请输入欢迎语：")

                setView(view)
                setPositiveButton("确认") { dialog, which ->
                    val text = input.text.toString()
                    // 处理文本输入
                    binding.welcomeText.text = text
                    val editor =
                        activity?.getSharedPreferences("data", Context.MODE_PRIVATE)?.edit()
                    editor?.putString("welcometext", text)
                    editor?.apply()
                }
                setNegativeButton("取消") { dialog, which ->
                    dialog.cancel()
                }
                create()
                show()
            }

        }

        binding.timecard.setOnClickListener {
            CardDatePickerDialog.builder(view.context)

                .setTitle("请选择日期：")
                .showBackNow(false)
                .setBackGroundModel(R.drawable.shape_sheet_dialog_bg)
                .setDisplayType(DateTimeConfig.HOUR, DateTimeConfig.MIN)
                .showFocusDateInfo(false)
                .setPickerLayout(R.layout.layout_time_picker_segmentation)
                .setThemeColor(Color.parseColor("#3EB06A"))
                .setAssistColor(
                    if (BaseUtil.isDarkTheme(view.context)) Color.parseColor("#707070") else Color.parseColor(
                        "#b9b9b9"
                    )
                )
                .setOnChoose { millisecond ->

                    val dateString = BaseUtil.second2Date(millisecond)
                    val hour = dateString.substring(8..9).toInt()
                    val min = dateString.substring(10..11).toInt()
                    val editor =
                        activity?.getSharedPreferences("data", Context.MODE_PRIVATE)?.edit()
                    editor?.putInt("hour", hour)
                    editor?.putInt("minute", min)
                    editor?.apply()
                    binding.noticeTimeText.text = "$hour:$min"

                }.build().show()
        }
    }
}
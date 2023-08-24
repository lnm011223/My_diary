package com.lnm011223.my_diary.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.lnm011223.my_diary.MainViewModel
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.databinding.FragmentSettingsBinding
import com.lnm011223.my_diary.util.BaseUtil
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog

// TODO: 添加深色模式开关按钮
// TODO: 可以考虑适配material3样式
class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null
    private lateinit var mainViewModel: MainViewModel

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
        mainViewModel =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("CommitPrefEdits", "SetTextI18n", "InflateParams", "ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = activity?.getSharedPreferences("data", Context.MODE_PRIVATE)
        val isNotice = prefs?.getBoolean("isNotice", false)
        val welcometext = prefs?.getString("welcometext", "天天开心！")
        val qqtext = prefs?.getString("qqtext", "")
        binding.changeQQBtn.text = qqtext
        var themeid = prefs?.getInt("themeid", 2)
        binding.isNoticeButton.isChecked = isNotice!!
        binding.welcomeText.text = welcometext
        binding.changeThemeBtn.text = when (themeid) {
            0 -> "浅色模式"
            1 -> "深色模式"
            2 -> "跟随系统"
            else -> {
                "跟随系统"
            }
        }
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
        if (qqtext != "") {
            Glide.with(this)
                .load("https://q1.qlogo.cn/g?b=qq&nk=$qqtext&s=640")
                .into(binding.avatar)
        }
        binding.avatar.setOnClickListener {
            val moodMap = mapOf(
                1 to R.drawable.mood_1,
                2 to R.drawable.mood_2,
                3 to R.drawable.mood_3,
                4 to R.drawable.mood_4,
                5 to R.drawable.mood_5,
            )
            val mood_num = (1..5).random()
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
        binding.sloganText.text = mainViewModel.yiyan
        binding.changeUsernameBtn.setOnClickListener {
            // 设置布局
            val view = layoutInflater.inflate(R.layout.welcometext_dialog_layout, null)
            val input = view.findViewById<EditText>(R.id.input_text)

            AlertDialog.Builder(requireActivity()).apply {

                setTitle("请输入欢迎语：")

                setView(view)
                input.setText(welcometext)
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
        binding.changeQQBtn.setOnClickListener {
            // 设置布局
            val view = layoutInflater.inflate(R.layout.welcometext_dialog_layout, null)
            val input = view.findViewById<EditText>(R.id.input_text)

            AlertDialog.Builder(requireActivity()).apply {

                setTitle("请输入QQ号，头像会设置成QQ头像：")
                setView(view)
                input.setText(qqtext)
                setPositiveButton("确认") { dialog, which ->
                    val text = input.text.toString()
                    // 处理文本输入
                    binding.changeQQBtn.text = text
                    val editor =
                        activity?.getSharedPreferences("data", Context.MODE_PRIVATE)?.edit()
                    editor?.putString("qqtext", text)
                    editor?.apply()
                    Glide.with(requireActivity())
                        .load("https://q1.qlogo.cn/g?b=qq&nk=$text&s=640")
                        .into(binding.avatar)
                }
                setNegativeButton("取消") { dialog, which ->
                    dialog.cancel()
                }
                create()
                show()
            }

        }
        binding.changeThemeBtn.setOnClickListener {

            val options = arrayOf("浅色模式", "深色模式", "跟随系统") // 选项列表

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("应用主题")
            builder.setSingleChoiceItems(options, themeid!!) { dialog, which ->
                // 在点击选项时触发的回调函数，which 表示所选选项的索引
                // 这里可以根据所选选项执行相应的操作
                // 例如：Toast 提示选中的选项
                val editor =
                    activity?.getSharedPreferences("data", Context.MODE_PRIVATE)?.edit()
                editor?.putInt("themeid", which)
                editor?.apply()
                themeid = which
                binding.changeThemeBtn.text = when (themeid) {
                    0 -> "浅色模式"
                    1 -> "深色模式"
                    2 -> "跟随系统"
                    else -> {
                        "跟随系统"
                    }
                }
//                Toast.makeText(requireContext(), "Selected: ${options[which]}", Toast.LENGTH_SHORT)
//                    .show()
                val intent =
                    requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
                intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                requireContext().startActivity(intent)

                dialog.dismiss() // 关闭对话框
            }

            builder.setPositiveButton("OK") { dialog, which ->
                // 点击“确定”按钮后触发的回调函数
                // 这里可以处理确认操作，或者忽略该回调函数
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                // 点击“取消”按钮后触发的回调函数
                // 这里可以处理取消操作，或者忽略该回调函数
            }

            val dialog = builder.create()
            dialog.show()

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

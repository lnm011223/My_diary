package com.lnm011223.my_diary.ui.dashboard

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.net.toUri
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.databinding.DialogMyBottomSheetBinding

/**

 * @Author liangnuoming
 * @Date 2022/6/23-6:36 下午
 * 日记图片详情预览

 */
class ImageBottomSheet(private val imageUrl: String, private val date: String) :
    BottomSheetDialogFragment() {
    private lateinit var binding: DialogMyBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        //拿到系统的 bottom_sheet
        val view: FrameLayout = dialog?.findViewById(R.id.design_bottom_sheet)!!
        //设置view高度
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        //获取behavior
        val behavior = BottomSheetBehavior.from(view)
        //设置弹出高度
        behavior.peekHeight = 3000
        //设置展开状态
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogMyBottomSheetBinding.inflate(inflater, container, false)
        initView(imageUrl, date)
        return binding.root
    }

    private fun initView(imageUrl: String, date: String) {
        binding.sheetToolBar.subtitle = "${date.substring(0..3)}年 ${date.substring(4..5)}月 ${date.substring(6..7)}日"
        //do something
        //rootView.tv_cancel.setOnClickListener { dismiss() }
        binding.sheetImage.setImageURI(imageUrl.toUri())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sheetToolBar.setNavigationOnClickListener {

        }
    }


}


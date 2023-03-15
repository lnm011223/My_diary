package com.lnm011223.my_diary.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
        binding.sheetToolBar.subtitle = date
        //do something
        //rootView.tv_cancel.setOnClickListener { dismiss() }
        binding.sheetImage.setImageURI(imageUrl.toUri())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}


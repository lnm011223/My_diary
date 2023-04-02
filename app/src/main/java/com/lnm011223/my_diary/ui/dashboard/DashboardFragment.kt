package com.lnm011223.my_diary.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnm011223.my_diary.*
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.databinding.FragmentDashboardBinding
import com.lnm011223.my_diary.logic.model.Diary
import kotlin.concurrent.thread


// TODO: 优化recyclerview的滑动卡顿
class DashboardFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentDashboardBinding? = null
    val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)
    private val binding get() = _binding!!
    val moodMap = mapOf(
        R.drawable.mood_1 to 1,
        R.drawable.mood_2 to 2,
        R.drawable.mood_3 to 3,
        R.drawable.mood_4 to 4,
        R.drawable.mood_5 to 5,
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (mainViewModel.selectflag) {
            Log.d("yes", "yes")
            when {
                mainViewModel.flag1 -> {
                    binding.selectMood1.setImageResource(R.drawable.mood_1)
                }
                mainViewModel.flag2 -> {
                    binding.selectMood2.setImageResource(R.drawable.mood_2)
                }
                mainViewModel.flag3 -> {
                    binding.selectMood3.setImageResource(R.drawable.mood_3)
                }
                mainViewModel.flag4 -> {
                    binding.selectMood4.setImageResource(R.drawable.mood_4)
                }
                mainViewModel.flag5 -> {
                    binding.selectMood5.setImageResource(R.drawable.mood_5)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDiary()
        val layoutManager = LinearLayoutManager(context)
        binding.diaryRecycle.layoutManager = layoutManager
        val adapter = DiaryAdapter(mainViewModel.diaryList.value!!, requireActivity())
//        val mDividerItemDecoration = DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL)
//        binding.diaryRecycle.addItemDecoration(mDividerItemDecoration)
        binding.diaryRecycle.adapter = adapter

        mainViewModel.addPosition.observe(viewLifecycleOwner) { add ->
            when (add) {
                -1 -> {}
                1 -> {
                    when {
                        //当新的mood和当前筛选的一样才加进recyclerview
                        mainViewModel.addDiaryItem.moon == mainViewModel.selectid && mainViewModel.selectflag -> {
                            mainViewModel.addDiary(mainViewModel.addDiaryItem)
                            adapter.notifyItemInserted(mainViewModel.diaryList.value!!.size - 1)
                            binding.diaryRecycle.smoothScrollToPosition(adapter.itemCount - 1)
                        }
                        //当前没筛选才添加
                        !mainViewModel.selectflag -> {
                            mainViewModel.addDiary(mainViewModel.addDiaryItem)
                            adapter.notifyItemInserted(mainViewModel.diaryList.value!!.size - 1)
                            binding.diaryRecycle.smoothScrollToPosition(adapter.itemCount - 1)
                        }
                        //重置flag值
                        else -> mainViewModel.addPosition.value = -1
                    }


                }
            }
        }
        mainViewModel.revisePosition.observe(viewLifecycleOwner) { revisePosition ->
            when (revisePosition) {
                -1 -> {}
                else -> {
                    //当检测到有变化才改变
                    mainViewModel.changeDiary(revisePosition, mainViewModel.reviseDiaryItem)
                    adapter.notifyItemChanged(revisePosition)
                    binding.diaryRecycle.scrollToPosition(revisePosition)
                    mainViewModel.revisePosition.value = -1
                }
            }


        }
        //recyclerview点击接口回调
        adapter.setOnItemClickListener(object : DiaryAdapter.ItemListenter {
            override fun deleteItemClick(position: Int) {
                mainViewModel.deleteDiary(position)
                adapter.notifyItemRemoved(position)
            }

            override fun reviseItemClick(position: Int) {

            }

            override fun showItemImageClick(position: Int) {

                ImageBottomSheet(
                    adapter.diaryList[position].diary_image,
                    adapter.diaryList[position].date_text
                ).show(fragmentManager!!, "ImageBottomSheet")
                Log.d("111", adapter.diaryList[position].diary_image)
                //ImageBottomSheet().setImageShow(adapter.diaryList[position].diary_image)

            }


        })

        var imageViewList = listOf(
            binding.selectMood1,
            binding.selectMood2,
            binding.selectMood3,
            binding.selectMood4,
            binding.selectMood5
        )
        var flagList = mutableListOf(
            mainViewModel.flag1,
            mainViewModel.flag2,
            mainViewModel.flag3,
            mainViewModel.flag4,
            mainViewModel.flag5
        )
        val moodIdList = listOf(
            R.drawable.mood_1_last,
            R.drawable.mood_2_last,
            R.drawable.mood_3_last,
            R.drawable.mood_4_last,
            R.drawable.mood_5_last
        )


        //筛选逻辑
        binding.selectMood1.setOnClickListener {

            mainViewModel.flag1 = moodClick(
                mainViewModel.flag1,
                binding.selectMood1,
                R.drawable.mood_1,
                R.drawable.mood_1_last,
                imageViewList,
                flagList,
                moodIdList
            )
            if (mainViewModel.flag1) {
                adapter.notifyDataSetChanged()
            }

        }
        binding.selectMood2.setOnClickListener {
            mainViewModel.flag2 = moodClick(
                mainViewModel.flag2,
                binding.selectMood2,
                R.drawable.mood_2,
                R.drawable.mood_2_last,
                imageViewList,
                flagList,
                moodIdList
            )
            if (mainViewModel.flag2) {
                adapter.notifyDataSetChanged()
            }

        }
        binding.selectMood3.setOnClickListener {

            mainViewModel.flag3 = moodClick(
                mainViewModel.flag3,
                binding.selectMood3,
                R.drawable.mood_3,
                R.drawable.mood_3_last,
                imageViewList,
                flagList,
                moodIdList
            )
            if (mainViewModel.flag3) {
                adapter.notifyDataSetChanged()
            }
        }
        binding.selectMood4.setOnClickListener {

            mainViewModel.flag4 = moodClick(
                mainViewModel.flag4,
                binding.selectMood4,
                R.drawable.mood_4,
                R.drawable.mood_4_last,
                imageViewList,
                flagList,
                moodIdList
            )
            if (mainViewModel.flag4) {
                adapter.notifyDataSetChanged()
            }
        }
        binding.selectMood5.setOnClickListener {

            mainViewModel.flag5 = moodClick(
                mainViewModel.flag5,
                binding.selectMood5,
                R.drawable.mood_5,
                R.drawable.mood_5_last,
                imageViewList,
                flagList,
                moodIdList
            )
            if (mainViewModel.flag5) {
                adapter.notifyDataSetChanged()
            }
        }

        //binding.cancelselectBtn.paintFlags = (binding.cancelselectBtn.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
        binding.cancelselectBtn.setOnClickListener {
            mainViewModel.selectflag = false
            binding.apply {
                selectMood1.setImageResource(R.drawable.mood_1_last)
                selectMood2.setImageResource(R.drawable.mood_2_last)
                selectMood3.setImageResource(R.drawable.mood_3_last)
                selectMood4.setImageResource(R.drawable.mood_4_last)
                selectMood5.setImageResource(R.drawable.mood_5_last)

            }
            initDiary()
            adapter.notifyDataSetChanged()
        }


    }

    @SuppressLint("Range")
    private fun initDiary() {
        thread {
            //mainViewModel.clearAll()
            //diaryList.clear()
            val diaryList = ArrayList<Diary>()
            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("select * from diarydata ", null)


            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex("id")).toInt()
                    val datetext = cursor.getString(cursor.getColumnIndex("datetext"))
                    val imageuri = cursor.getString(cursor.getColumnIndex("imageuri"))
                    val moodid = cursor.getInt(cursor.getColumnIndex("moodid"))
                    val diarytext = cursor.getString(cursor.getColumnIndex("diarytext"))
                    if (mainViewModel.selectflag) {
                        if (moodMap[mainViewModel.selectid] == moodid) {
                            diaryList.add(Diary(id, datetext, moodid, imageuri, diarytext))
                        }
                    } else {
                        diaryList.add(Diary(id, datetext, moodid, imageuri, diarytext))
                    }

                } while (cursor.moveToNext())
            }
            cursor.close()
            mainViewModel.diaryList.value?.clear()
            mainViewModel.setAll(diaryList)
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
            mainViewModel.selectid = mood
            mainViewModel.selectflag = true
            initDiary()
            !flag

        } else {
            moodImage.setImageResource(mood_last)
            !flag
        }
    }

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


//


}
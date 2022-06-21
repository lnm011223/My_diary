package com.lnm011223.my_diary.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnm011223.my_diary.*
import com.lnm011223.my_diary.databinding.FragmentDashboardBinding
import kotlin.concurrent.thread


// TODO: 优化recycleview的屎山代码
class DashboardFragment : Fragment() {
    private lateinit var diaryViewModel: DiaryViewModel
    private var _binding: FragmentDashboardBinding? = null
    val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        diaryViewModel =
            ViewModelProvider(requireActivity()).get(DiaryViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (diaryViewModel.selectflag) {
            Log.d("yes","yes")
            when {
                diaryViewModel.flag1 -> { binding.selectMood1.setImageResource(R.drawable.mood_1) }
                diaryViewModel.flag2 -> { binding.selectMood2.setImageResource(R.drawable.mood_2) }
                diaryViewModel.flag3 -> { binding.selectMood3.setImageResource(R.drawable.mood_3) }
                diaryViewModel.flag4 -> { binding.selectMood4.setImageResource(R.drawable.mood_4) }
                diaryViewModel.flag5 -> { binding.selectMood5.setImageResource(R.drawable.mood_5) }
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
        val adapter = DiaryAdapter(diaryViewModel.diaryList.value!!, requireActivity())
        binding.diaryRecycle.adapter = adapter
        diaryViewModel.addPosition.observe(viewLifecycleOwner) { add ->
            when (add) {
                -1 -> {}
                1 -> {
                    when {
                        //当新的mood和当前筛选的一样才加进recyclerview
                        diaryViewModel.addDiaryItem.moon == diaryViewModel.selectid && diaryViewModel.selectflag -> {
                            diaryViewModel.addDiary(diaryViewModel.addDiaryItem)
                            adapter.notifyItemInserted(diaryViewModel.diaryList.value!!.size-1)
                            binding.diaryRecycle.smoothScrollToPosition(adapter.itemCount-1)
                        }
                        //当前没筛选才添加
                        !diaryViewModel.selectflag -> {
                            diaryViewModel.addDiary(diaryViewModel.addDiaryItem)
                            adapter.notifyItemInserted(diaryViewModel.diaryList.value!!.size-1)
                            binding.diaryRecycle.smoothScrollToPosition(adapter.itemCount-1)
                        }
                        //重置flag值
                        else -> diaryViewModel.addPosition.value = -1
                    }



                }
            }
        }
        diaryViewModel.revisePosition.observe(viewLifecycleOwner) { revisePosition ->
            when (revisePosition) {
                -1 -> { }
                else -> {
                    //当检测到有变化才改变
                    diaryViewModel.changeDiary(revisePosition,diaryViewModel.reviseDiaryItem)
                    adapter.notifyItemChanged(revisePosition)
                    binding.diaryRecycle.scrollToPosition(revisePosition)
                    diaryViewModel.revisePosition.value = -1
                }
            }


        }
        //recyclerview点击接口回调
        adapter.setOnItemClickListener(object : DiaryAdapter.ItemListenter {
            override fun deleteItemClick(position: Int) {
                diaryViewModel.deleteDiary(position)
                adapter.notifyItemRemoved(position)
            }

            override fun reviseItemClick(position: Int) {

            }


        })
        //筛选逻辑
        binding.selectMood1.setOnClickListener {
            if (!diaryViewModel.flag1) {
                binding.selectMood1.setImageResource(R.drawable.mood_1)
                diaryViewModel.flag1 = true
                changeOther(binding.selectMood1,diaryViewModel.flag1)
                diaryViewModel.selectid = R.drawable.mood_1
                diaryViewModel.selectflag = true
                initDiary()
                adapter.notifyDataSetChanged()
            }else{
                binding.selectMood1.setImageResource(R.drawable.mood_1_last)
                diaryViewModel.flag1 = false

            }
        }
        binding.selectMood2.setOnClickListener {
            if (!diaryViewModel.flag2) {
                binding.selectMood2.setImageResource(R.drawable.mood_2)
                diaryViewModel.flag2 = true
                changeOther(binding.selectMood2,diaryViewModel.flag2)
                diaryViewModel.selectid = R.drawable.mood_2
                diaryViewModel.selectflag = true
                initDiary()
                adapter.notifyDataSetChanged()
            }else{
                binding.selectMood2.setImageResource(R.drawable.mood_2_last)
                diaryViewModel.flag2 = false
            }
        }
        binding.selectMood3.setOnClickListener {
            if (!diaryViewModel.flag3) {
                binding.selectMood3.setImageResource(R.drawable.mood_3)
                diaryViewModel.flag3 = true
                changeOther(binding.selectMood3,diaryViewModel.flag3)
                diaryViewModel.selectid = R.drawable.mood_3
                diaryViewModel.selectflag = true
                initDiary()
                adapter.notifyDataSetChanged()
            }else{
                binding.selectMood3.setImageResource(R.drawable.mood_3_last)
                diaryViewModel.flag3 = false
            }
        }
        binding.selectMood4.setOnClickListener {
            if (!diaryViewModel.flag4) {
                binding.selectMood4.setImageResource(R.drawable.mood_4)
                diaryViewModel.flag4 = true
                changeOther(binding.selectMood4,diaryViewModel.flag4)
                diaryViewModel.selectid = R.drawable.mood_4
                diaryViewModel.selectflag = true
                initDiary()
                adapter.notifyDataSetChanged()
                Log.d("livedata",diaryViewModel.diaryList.toString())
            }else{
                binding.selectMood4.setImageResource(R.drawable.mood_4_last)
                diaryViewModel.flag4 = false
            }
        }
        binding.selectMood5.setOnClickListener {
            if (!diaryViewModel.flag5) {
                binding.selectMood5.setImageResource(R.drawable.mood_5)
                diaryViewModel.flag5 = true
                changeOther(binding.selectMood5,diaryViewModel.flag5)
                diaryViewModel.selectid = R.drawable.mood_5
                diaryViewModel.selectflag = true
                initDiary()
                adapter.notifyDataSetChanged()
            }else{
                binding.selectMood5.setImageResource(R.drawable.mood_5_last)
                diaryViewModel.flag5 = false
            }
        }
        binding.cancelselectBtn.setOnClickListener {
            diaryViewModel.selectflag = false
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
    private fun initDiary(){
        thread {
            //diaryViewModel.clearAll()
            //diaryList.clear()
            var diaryList = ArrayList<Diary>()
            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("select * from diarydata ", null)


            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex("id")).toInt()
                    val datetext = cursor.getString(cursor.getColumnIndex("datetext"))
                    val imageuri = cursor.getString(cursor.getColumnIndex("imageuri"))
                    val moodid = cursor.getInt(cursor.getColumnIndex("moodid"))
                    val diarytext = cursor.getString(cursor.getColumnIndex("diarytext"))
                    if (diaryViewModel.selectflag){
                        if (diaryViewModel.selectid==moodid){
                            diaryList.add(Diary(id,datetext,moodid,imageuri,diarytext))
                        }
                    }else{
                        diaryList.add(Diary(id,datetext,moodid,imageuri,diarytext))
                    }

                }while (cursor.moveToNext())
            }
            cursor.close()
            diaryViewModel.diaryList.value?.clear()
            diaryViewModel.setAll(diaryList)
        }
    }


    private fun changeOther(imageView: ImageView, flag:Boolean){

        if (diaryViewModel.selectflag){
            when (imageView) {
                binding.selectMood1 -> {
                    binding.apply {
                        selectMood2.setImageResource(R.drawable.mood_2_last)
                        selectMood3.setImageResource(R.drawable.mood_3_last)
                        selectMood4.setImageResource(R.drawable.mood_4_last)
                        selectMood5.setImageResource(R.drawable.mood_5_last)

                    }
                    diaryViewModel.flag2 = false
                    diaryViewModel.flag3 = false
                    diaryViewModel.flag4 = false
                    diaryViewModel.flag5 = false

                }
                binding.selectMood2 -> {
                    binding.apply {
                        selectMood1.setImageResource(R.drawable.mood_1_last)
                        selectMood3.setImageResource(R.drawable.mood_3_last)
                        selectMood4.setImageResource(R.drawable.mood_4_last)
                        selectMood5.setImageResource(R.drawable.mood_5_last)

                    }
                    diaryViewModel.flag1 = false
                    diaryViewModel.flag3 = false
                    diaryViewModel.flag4 = false
                    diaryViewModel.flag5 = false

                }
                binding.selectMood3 -> {
                    binding.apply {
                        selectMood2.setImageResource(R.drawable.mood_2_last)
                        selectMood1.setImageResource(R.drawable.mood_1_last)
                        selectMood4.setImageResource(R.drawable.mood_4_last)
                        selectMood5.setImageResource(R.drawable.mood_5_last)

                    }
                    diaryViewModel.flag2 = false
                    diaryViewModel.flag1 = false
                    diaryViewModel.flag4 = false
                    diaryViewModel.flag5 = false

                }
                binding.selectMood4 -> {
                    binding.apply {
                        selectMood2.setImageResource(R.drawable.mood_2_last)
                        selectMood3.setImageResource(R.drawable.mood_3_last)
                        selectMood1.setImageResource(R.drawable.mood_1_last)
                        selectMood5.setImageResource(R.drawable.mood_5_last)

                    }
                    diaryViewModel.flag2 = false
                    diaryViewModel.flag3 = false
                    diaryViewModel.flag1 = false
                    diaryViewModel.flag5 = false

                }
                binding.selectMood5 -> {
                    binding.apply {
                        selectMood2.setImageResource(R.drawable.mood_2_last)
                        selectMood3.setImageResource(R.drawable.mood_3_last)
                        selectMood4.setImageResource(R.drawable.mood_4_last)
                        selectMood1.setImageResource(R.drawable.mood_1_last)

                    }
                    diaryViewModel.flag2 = false
                    diaryViewModel.flag3 = false
                    diaryViewModel.flag4 = false
                    diaryViewModel.flag1 = false

                }
            }
        }
    }


}
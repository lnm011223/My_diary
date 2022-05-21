package com.lnm011223.my_diary.ui.dashboard

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lnm011223.my_diary.*
import com.lnm011223.my_diary.databinding.FragmentDashboardBinding
import kotlin.concurrent.thread


class DashboardFragment : Fragment() {
    var selectid = R.drawable.mood_1
    var flag1 = false
    var flag2 = false
    var flag3 = false
    var flag4 = false
    var flag5 = false
    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val diaryList = ArrayList<Diary>()
    val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)
    lateinit var receiver: DiaryDataChangeReceiver
    var selectflag = false
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        Log.d("aaa","resume")
        val intentFilter = IntentFilter()
        intentFilter.addAction("DiaryDataChangeReceiver")
        receiver = DiaryDataChangeReceiver()
        activity?.registerReceiver(receiver,intentFilter)
    }

    override fun onPause() {
        super.onPause()
        Log.d("aaa","pause")
        activity?.unregisterReceiver(receiver)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        initDiary()
        val layoutManager = LinearLayoutManager(context)
        binding.diaryRecycle.layoutManager = layoutManager
        val adapter = DiaryAdapter(diaryList)
        binding.diaryRecycle.adapter = adapter

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.selectMood1.setOnClickListener {
            if (flag1 == false) {
                binding.selectMood1.setImageResource(R.drawable.mood_1)
                flag1 = true
                changeOther(binding.selectMood1,flag1)
                selectid = R.drawable.mood_1
                selectflag = true
                initDiary()
                val layoutManager = LinearLayoutManager(context)
                binding.diaryRecycle.layoutManager = layoutManager
                val adapter = DiaryAdapter(diaryList)
                binding.diaryRecycle.adapter = adapter
            }else{
                binding.selectMood1.setImageResource(R.drawable.mood_1_last)
                flag1 = false

            }
        }
        binding.selectMood2.setOnClickListener {
            if (flag2 == false) {
                binding.selectMood2.setImageResource(R.drawable.mood_2)
                flag2 = true
                changeOther(binding.selectMood2,flag2)
                selectid = R.drawable.mood_2
                selectflag = true
                initDiary()
                val layoutManager = LinearLayoutManager(context)
                binding.diaryRecycle.layoutManager = layoutManager
                val adapter = DiaryAdapter(diaryList)
                binding.diaryRecycle.adapter = adapter
            }else{
                binding.selectMood2.setImageResource(R.drawable.mood_2_last)
                flag2 = false
            }
        }
        binding.selectMood3.setOnClickListener {
            if (flag3 == false) {
                binding.selectMood3.setImageResource(R.drawable.mood_3)
                flag3 = true
                changeOther(binding.selectMood3,flag3)
                selectid = R.drawable.mood_3
                selectflag = true
                initDiary()
                val layoutManager = LinearLayoutManager(context)
                binding.diaryRecycle.layoutManager = layoutManager
                val adapter = DiaryAdapter(diaryList)
                binding.diaryRecycle.adapter = adapter
            }else{
                binding.selectMood3.setImageResource(R.drawable.mood_3_last)
                flag3 = false
            }
        }
        binding.selectMood4.setOnClickListener {
            if (flag4 == false) {
                binding.selectMood4.setImageResource(R.drawable.mood_4)
                flag4 = true
                changeOther(binding.selectMood4,flag4)
                selectid = R.drawable.mood_4
                selectflag = true
                initDiary()
                val layoutManager = LinearLayoutManager(context)
                binding.diaryRecycle.layoutManager = layoutManager
                val adapter = DiaryAdapter(diaryList)
                binding.diaryRecycle.adapter = adapter
            }else{
                binding.selectMood4.setImageResource(R.drawable.mood_4_last)
                flag4 = false
            }
        }
        binding.selectMood5.setOnClickListener {
            if (flag5 == false) {
                binding.selectMood5.setImageResource(R.drawable.mood_5)
                flag5 = true
                changeOther(binding.selectMood5,flag5)
                selectid = R.drawable.mood_5
                selectflag = true
                initDiary()
                val layoutManager = LinearLayoutManager(context)
                binding.diaryRecycle.layoutManager = layoutManager
                val adapter = DiaryAdapter(diaryList)
                binding.diaryRecycle.adapter = adapter
            }else{
                binding.selectMood5.setImageResource(R.drawable.mood_5_last)
                flag5 = false
            }
        }
        binding.cancelselectBtn.setOnClickListener {
            selectflag = false
            initDiary()
            val layoutManager = LinearLayoutManager(context)
            binding.diaryRecycle.layoutManager = layoutManager
            val adapter = DiaryAdapter(diaryList)
            binding.diaryRecycle.adapter = adapter
        }



    }
    @SuppressLint("Range")
    private fun initDiary(){
        thread {
            diaryList.clear()
            val db = dbHelper.writableDatabase
            var cursor = db.rawQuery("select * from diarydata ", null)


            if (cursor.moveToFirst()) {
                do {
                    val datetext = cursor.getString(cursor.getColumnIndex("datetext"))
                    val imageuri = cursor.getString(cursor.getColumnIndex("imageuri"))
                    val moodid = cursor.getInt(cursor.getColumnIndex("moodid"))
                    val diarytext = cursor.getString(cursor.getColumnIndex("diarytext"))
                    if (selectflag){
                        if (selectid==moodid){
                            diaryList.add(Diary(datetext,moodid,imageuri,diarytext))
                        }
                    }else{
                        diaryList.add(Diary(datetext,moodid,imageuri,diarytext))
                    }

                }while (cursor.moveToNext())
            }
            cursor.close()
        }
    }
    inner class DiaryDataChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            initDiary()
            val layoutManager = LinearLayoutManager(context)
            binding.diaryRecycle.layoutManager = layoutManager
            val adapter = DiaryAdapter(diaryList)
            binding.diaryRecycle.adapter = adapter
        }
    }

    private fun changeOther(imageView: ImageView, flag:Boolean){

        if (flag==true){
            when (imageView) {
                binding.selectMood1 -> {
                    binding.apply {
                        selectMood2.setImageResource(R.drawable.mood_2_last)
                        selectMood3.setImageResource(R.drawable.mood_3_last)
                        selectMood4.setImageResource(R.drawable.mood_4_last)
                        selectMood5.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag4 = false
                    flag5 = false

                }
                binding.selectMood2 -> {
                    binding.apply {
                        selectMood1.setImageResource(R.drawable.mood_1_last)
                        selectMood3.setImageResource(R.drawable.mood_3_last)
                        selectMood4.setImageResource(R.drawable.mood_4_last)
                        selectMood5.setImageResource(R.drawable.mood_5_last)

                    }
                    flag1 = false
                    flag3 = false
                    flag4 = false
                    flag5 = false

                }
                binding.selectMood3 -> {
                    binding.apply {
                        selectMood2.setImageResource(R.drawable.mood_2_last)
                        selectMood1.setImageResource(R.drawable.mood_1_last)
                        selectMood4.setImageResource(R.drawable.mood_4_last)
                        selectMood5.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag1 = false
                    flag4 = false
                    flag5 = false

                }
                binding.selectMood4 -> {
                    binding.apply {
                        selectMood2.setImageResource(R.drawable.mood_2_last)
                        selectMood3.setImageResource(R.drawable.mood_3_last)
                        selectMood1.setImageResource(R.drawable.mood_1_last)
                        selectMood5.setImageResource(R.drawable.mood_5_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag1 = false
                    flag5 = false

                }
                binding.selectMood5 -> {
                    binding.apply {
                        selectMood2.setImageResource(R.drawable.mood_2_last)
                        selectMood3.setImageResource(R.drawable.mood_3_last)
                        selectMood4.setImageResource(R.drawable.mood_4_last)
                        selectMood1.setImageResource(R.drawable.mood_1_last)

                    }
                    flag2 = false
                    flag3 = false
                    flag4 = false
                    flag1 = false

                }
            }
        }
    }


}
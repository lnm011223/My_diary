package com.lnm011223.my_diary.ui.dashboard

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnm011223.my_diary.*
import com.lnm011223.my_diary.databinding.FragmentDashboardBinding
import kotlin.concurrent.thread


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val diaryList = ArrayList<Diary>()
    val dbHelper = MyDatabaseHelper(MyApplication.context,"DiaryData.db",1)

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




    }
    @SuppressLint("Range")
    private fun initDiary(){
        thread {
            diaryList.clear()
            val db = dbHelper.writableDatabase
            val cursor = db.query("diarydata",null,null,null,null,null,null,null)
            if (cursor.moveToFirst()) {
                do {
                    val datetext = cursor.getString(cursor.getColumnIndex("datetext"))
                    val imageuri = cursor.getString(cursor.getColumnIndex("imageuri"))
                    val moodid = cursor.getInt(cursor.getColumnIndex("moodid"))
                    val diarytext = cursor.getString(cursor.getColumnIndex("diarytext"))
                    diaryList.add(Diary(datetext,moodid,imageuri.toUri(),diarytext))
                }while (cursor.moveToNext())
            }
            cursor.close()
        }
    }





}
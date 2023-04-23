package com.lnm011223.my_diary.ui.todo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.lnm011223.my_diary.MainViewModel
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.logic.model.Todo
import com.lnm011223.my_diary.databinding.FragmentTodoBinding
import com.lnm011223.my_diary.logic.model.Diary
import kotlin.concurrent.thread

// TODO: 待办界面 
class TodoFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    val dbHelper = MyDatabaseHelper(MyApplication.context, "DiaryData.db", 1)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Deprecated("Deprecated in Java")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = AdapterFragmentPager(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "未完成"
                }
                1 -> {
                    tab.text = "已完成"
                }
            }
        }.attach()
        val linearLayout = binding.tabLayout.getChildAt(0) as? LinearLayout
        linearLayout?.let {
            it.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            it.dividerDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.shape_tab_divider)
            it.dividerPadding = 30
        }

        initUnFinishedList()

    }

    @SuppressLint("Range")
    private fun initUnFinishedList() {
        thread {
            val finishedList = ArrayList<Todo>()
            val unfinshedList = ArrayList<Todo>()
            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("select * from tododata ", null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex("id")).toInt()
                    val todotext = cursor.getString(cursor.getColumnIndex("todotext"))
                    val classification = cursor.getString(cursor.getColumnIndex("classification"))
                    val startdate = cursor.getString(cursor.getColumnIndex("startdate"))
                    val deadline = cursor.getString(cursor.getColumnIndex("deadline"))
                    val isTop = cursor.getInt(cursor.getColumnIndex("isTop"))
                    val isDone = cursor.getInt(cursor.getColumnIndex("isDone"))
                    val enddate = cursor.getString(cursor.getColumnIndex("enddate"))
                    when (isDone) {
                        0 -> {
                            when (isTop) {
                                0 -> {
                                    unfinshedList.add(
                                        Todo(
                                            id,
                                            todotext,
                                            classification,
                                            startdate,
                                            "0",
                                            deadline,
                                            isTop,
                                            0
                                        )
                                    )
                                }
                                1 -> {
                                    unfinshedList.add(
                                        0,
                                        Todo(
                                            id,
                                            todotext,
                                            classification,
                                            startdate,
                                            "0",
                                            deadline,
                                            isTop,
                                            0
                                        )
                                    )
                                }
                            }
                        }
                        1 -> {
                            when (isTop) {
                                0 -> {
                                    finishedList.add(
                                        Todo(
                                            id,
                                            todotext,
                                            classification,
                                            startdate,
                                            enddate,
                                            deadline,
                                            isTop,
                                            0
                                        )
                                    )
                                }
                                1 -> {
                                    finishedList.add(
                                        0,
                                        Todo(
                                            id,
                                            todotext,
                                            classification,
                                            startdate,
                                            enddate,
                                            deadline,
                                            isTop,
                                            0
                                        )
                                    )
                                }
                            }
                        }
                    }



                } while (cursor.moveToNext())
            }
            cursor.close()
            mainViewModel.unfinishedList.value?.clear()
            mainViewModel.setAllUnfinished(unfinshedList)
            mainViewModel.finishedList.value?.clear()
            mainViewModel.setAllfinished(finishedList)
        }
    }




}


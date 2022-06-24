package com.lnm011223.my_diary

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnm011223.my_diary.databinding.FragmentUnFinishedBinding


class UnFinishedFragment : Fragment() {

    private lateinit var binding: FragmentUnFinishedBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel =
            ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = FragmentUnFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        initUnFinishedList()
        binding.unFinishedRecyclerView.layoutManager = layoutManager
        val adapter = UnFinishedAdapter(mainViewModel.unFinishedList.value!!, requireActivity())
        binding.unFinishedRecyclerView.adapter = adapter

    }

    private fun initUnFinishedList(){
        mainViewModel.unFinishedList.value?.add(Todo(1,"做完Todo的功能做完Todo的功能做完Todo的功能做完Todo的功能做完Todo的功能","工作","1","2","明天",1,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"做完Todo的功能","工作","1","2","明天",0,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"做完Todo的功能","","1","2","",1,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"做完Todo的功能","","1","2","明天",0,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"做完Todo的功能","工作","1","2","",1,0))
    }
}
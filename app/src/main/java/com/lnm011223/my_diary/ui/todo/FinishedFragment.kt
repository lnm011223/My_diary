package com.lnm011223.my_diary.ui.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnm011223.my_diary.MainViewModel
import com.lnm011223.my_diary.databinding.FragmentFinishedBinding



class FinishedFragment : Fragment() {
    private lateinit var binding: FragmentFinishedBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        //initFinishedList()
        binding.finishedRecyclerView.layoutManager = layoutManager
        val adapter = FinishedAdapter(mainViewModel.finishedList.value!!, requireActivity())
        binding.finishedRecyclerView.adapter = adapter

    }


}
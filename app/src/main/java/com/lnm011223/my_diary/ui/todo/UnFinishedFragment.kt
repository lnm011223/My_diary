package com.lnm011223.my_diary.ui.todo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnm011223.my_diary.MainViewModel
import com.lnm011223.my_diary.base.MyApplication
import com.lnm011223.my_diary.base.MyDatabaseHelper
import com.lnm011223.my_diary.databinding.FragmentUnFinishedBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        //initUnFinishedList()
        binding.unFinishedRecyclerView.layoutManager = layoutManager
        val adapter = UnFinishedAdapter(mainViewModel.unfinishedList.value!!, requireActivity())
        binding.unFinishedRecyclerView.adapter = adapter
        mainViewModel.addTodoPosition.observe(viewLifecycleOwner) { add ->
            when (add) {
                -1 -> {}
                1 -> {
                    mainViewModel.addunfinishedTodo(mainViewModel.addunfinishedItem)
                    adapter.notifyItemInserted(mainViewModel.unfinishedList.value!!.size - 1)
                    binding.unFinishedRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                    mainViewModel.addTodoPosition.value = -1
                }


            }
        }
        mainViewModel.notDonePosition.observe(viewLifecycleOwner) { done ->
            when (done) {
                -1 -> {}
                1 -> {
                    adapter.notifyItemInserted(mainViewModel.unfinishedList.value?.size!!)
                    mainViewModel.notDonePosition.value = -1
                }


            }
        }
        mainViewModel.reviseTodoPosition.observe(viewLifecycleOwner) { revisePosition ->
            when (revisePosition) {
                -1 -> {}
                else -> {
                    //当检测到有变化才改变
                    mainViewModel.changeTodo(revisePosition, mainViewModel.reviseTodoItem)
                    adapter.notifyItemChanged(revisePosition)
                    binding.unFinishedRecyclerView.scrollToPosition(revisePosition)
                    mainViewModel.reviseTodoPosition.value = -1
                }
            }


        }
        adapter.setOnItemClickListener(object : UnFinishedAdapter.ItemListenter {
            override fun deleteItemLongClick(position: Int) {
                mainViewModel.deleteUnfinished(position)
                adapter.notifyItemRemoved(position)
            }

            override fun reviseItemClick(position: Int) {

            }

            override fun topItemClick(position: Int) {
                val todo = adapter.unFinishedList[position]

                lifecycleScope.launchWhenCreated {
                    mainViewModel.unfinishedList.value?.removeAt(position)
                    adapter.notifyItemRemoved(position)

                    binding.unFinishedRecyclerView.smoothScrollToPosition(0)
                    delay(100L)
                    mainViewModel.unfinishedList.value?.add(0, todo)
                    adapter.notifyItemInserted(0)
                    binding.unFinishedRecyclerView.smoothScrollToPosition(0)


                }
            }

            override fun noTopItemClick(position: Int) {

            }

            override fun finishItemClick(position: Int) {
                val todo = adapter.unFinishedList[position]
                todo.isDone = 1
                mainViewModel.unfinishedList.value?.removeAt(position)
                adapter.notifyItemRemoved(position)
                mainViewModel.finishedList.value?.add(todo)
                mainViewModel.isDonePosition.value = 1
            }

        })
    }
}





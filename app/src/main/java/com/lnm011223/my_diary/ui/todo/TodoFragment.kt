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
import com.lnm011223.my_diary.logic.model.Todo
import com.lnm011223.my_diary.databinding.FragmentTodoBinding

// TODO: 待办界面 
class TodoFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentTodoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       //val animMap = mapOf("未完成" to R.drawable.ic_twotone_library_add_24, "已完成" to R.drawable.ic_twotone_library_add_check_24)

//
//        animMap.keys.forEach { s ->
//            val tab = binding.tabLayout.newTab()
//            val view = LayoutInflater.from(context).inflate(R.layout.item_tab, null)
//            val imageView = view.findViewById<ImageView>(R.id.item_icon)
//            val textView = view.findViewById<TextView>(R.id.item_title)
//            imageView.setImageResource(animMap[s]!!)
//            imageView.imageTintList = resources.getColorStateList(R.color.selector_color)
//            textView.text = s
//
//            tab.customView = view
//            binding.tabLayout.addTab(tab)
//        }
//
//        binding.tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                tab?.customView.let {
//
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//        })
        binding.viewPager.adapter = AdapterFragmentPager(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            val view = LayoutInflater.from(context).inflate(R.layout.item_tab, null)
//            val imageView = view.findViewById<ImageView>(R.id.item_icon)
//            val textView = view.findViewById<TextView>(R.id.item_title)
//            //val badgetext = view.findViewById<TextView>(R.id.item_badge)
//            tab.customView = view
            when (position){
                0 -> {
//                    imageView.setImageResource(R.drawable.ic_twotone_library_add_24)
//                    textView.text = "未完成"
                    //badgetext.text = "999"

                    tab.text = "未完成"
                    //tab.setIcon(R.drawable.ic_twotone_library_add_24)


                }
                1 -> {
//                    imageView.setImageResource(R.drawable.ic_twotone_library_add_check_24)
//                    textView.text = "已完成"
                    //badgetext.text = "999"
//                    imageView.setImageResource(R.drawable.ic_twotone_library_add_check_24)
//                    textView.text = "已完成"
//                    badgetext.visibility = View.GONE
                    tab.text = "已完成"
                    //tab.setIcon(R.drawable.ic_twotone_library_add_check_24)
                }
            }
        }.attach()
        val linearLayout = binding.tabLayout.getChildAt(0) as? LinearLayout
        linearLayout?.let {
            it.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            it.dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_tab_divider)
            it.dividerPadding = 30
        }
        initFinishedList()
        initUnFinishedList()
//        mainViewModel.unFinishedList.observe(viewLifecycleOwner) {
//            Log.d("dddtest",mainViewModel.unFinishedList.value!!.size.toString())
//            binding.tabLayout.getTabAt(0)?.let { tab ->
//
//                tab.orCreateBadge.apply {
//                    backgroundColor = Color.parseColor("#61AE72")
//                    maxCharacterCount = 3
//                    badgeGravity = BadgeDrawable.TOP_START
//                    number = mainViewModel.unFinishedList.value!!.size
//                    badgeTextColor = Color.WHITE
//
//
//                }
//            }
//
//            binding.tabLayout.getTabAt(1)?.let { tab ->
//
//                tab.orCreateBadge.apply {
//                    backgroundColor = Color.parseColor("#61AE72")
//                    maxCharacterCount = 3
//                    number = mainViewModel.finishedList.value!!.size
//                    badgeTextColor = Color.WHITE
//
//
//                }
//            }
//        }





    }

    private fun initUnFinishedList(){
        mainViewModel.unFinishedList.value?.add(Todo(1,"做完Todo的功能做完Todo的功能做完Todo的功能做完Todo的功能做完Todo的功能","工作","1","2","明天",1,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"做完Todo的功能","工作","1","2","明天",0,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"做完Todo的功能","","1","2","",1,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"做完Todo的功能","","1","2","明天",0,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"做完Todo的功能","工作","1","2","",1,0))
    }


    private fun initFinishedList(){
        mainViewModel.finishedList.value?.add(Todo(1,"做完Todo的功能做完Todo的功能做完Todo的功能做完Todo的功能做完Todo的功能","工作","1","2","明天",1,0))
        mainViewModel.finishedList.value?.add(Todo(1,"做完Todo的功能","工作","1","2","明天",0,0))
        mainViewModel.finishedList.value?.add(Todo(1,"做完Todo的功能","","1","2","",1,0))
        mainViewModel.finishedList.value?.add(Todo(1,"做完Todo的功能","","1","2","明天",0,0))
        mainViewModel.finishedList.value?.add(Todo(1,"做完Todo的功能","工作","1","2","",1,0))
    }



}
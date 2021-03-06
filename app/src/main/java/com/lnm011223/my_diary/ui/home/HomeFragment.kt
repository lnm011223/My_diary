package com.lnm011223.my_diary.ui.home

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lnm011223.my_diary.AdapterFragmentPager
import com.lnm011223.my_diary.MainViewModel
import com.lnm011223.my_diary.R
import com.lnm011223.my_diary.Todo
import com.lnm011223.my_diary.databinding.FragmentHomeBinding

// TODO: εΎεηι’ 
class HomeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null

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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
       //val animMap = mapOf("ζͺε?ζ" to R.drawable.ic_twotone_library_add_24, "ε·²ε?ζ" to R.drawable.ic_twotone_library_add_check_24)

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
//                    textView.text = "ζͺε?ζ"
                    //badgetext.text = "999"

                    tab.text = "ζͺε?ζ"
                    //tab.setIcon(R.drawable.ic_twotone_library_add_24)


                }
                1 -> {
//                    imageView.setImageResource(R.drawable.ic_twotone_library_add_check_24)
//                    textView.text = "ε·²ε?ζ"
                    //badgetext.text = "999"
//                    imageView.setImageResource(R.drawable.ic_twotone_library_add_check_24)
//                    textView.text = "ε·²ε?ζ"
//                    badgetext.visibility = View.GONE
                    tab.text = "ε·²ε?ζ"
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
        mainViewModel.unFinishedList.value?.add(Todo(1,"εε?Todoηεθ½εε?Todoηεθ½εε?Todoηεθ½εε?Todoηεθ½εε?Todoηεθ½","ε·₯δ½","1","2","ζε€©",1,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"εε?Todoηεθ½","ε·₯δ½","1","2","ζε€©",0,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"εε?Todoηεθ½","","1","2","",1,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"εε?Todoηεθ½","","1","2","ζε€©",0,0))
        mainViewModel.unFinishedList.value?.add(Todo(1,"εε?Todoηεθ½","ε·₯δ½","1","2","",1,0))
    }


    private fun initFinishedList(){
        mainViewModel.finishedList.value?.add(Todo(1,"εε?Todoηεθ½εε?Todoηεθ½εε?Todoηεθ½εε?Todoηεθ½εε?Todoηεθ½","ε·₯δ½","1","2","ζε€©",1,0))
        mainViewModel.finishedList.value?.add(Todo(1,"εε?Todoηεθ½","ε·₯δ½","1","2","ζε€©",0,0))
        mainViewModel.finishedList.value?.add(Todo(1,"εε?Todoηεθ½","","1","2","",1,0))
        mainViewModel.finishedList.value?.add(Todo(1,"εε?Todoηεθ½","","1","2","ζε€©",0,0))
        mainViewModel.finishedList.value?.add(Todo(1,"εε?Todoηεθ½","ε·₯δ½","1","2","",1,0))
    }



}
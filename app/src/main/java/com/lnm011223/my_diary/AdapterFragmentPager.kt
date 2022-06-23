package com.lnm011223.my_diary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

/**

 * @Author liangnuoming
 * @Date 2022/6/24-1:55 上午

 */
class AdapterFragmentPager(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGE_Finished -> FinishedFragment()
            PAGE_UnFinished -> UnFinishedFragment()
            else -> Fragment()

        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    companion object {
        const val PAGE_Finished = 0
        const val PAGE_UnFinished = 1

    }
}

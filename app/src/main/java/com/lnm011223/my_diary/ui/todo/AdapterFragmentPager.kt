package com.lnm011223.my_diary.ui.todo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**

 * @Author liangnuoming
 * @Date 2022/6/24-1:55 上午

 */
class AdapterFragmentPager(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {

            PAGE_UnFinished -> UnFinishedFragment()
            PAGE_Finished -> FinishedFragment()
            else -> Fragment()

        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    companion object {
        const val PAGE_Finished = 1
        const val PAGE_UnFinished = 0

    }
}

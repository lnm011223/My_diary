package com.lnm011223.my_diary.ui.todo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**

 * @Author liangnuoming
 * @Date 2022/6/24-1:55 上午

 */

//之前使用了fragmentactivity作为参数，导致tab里的fragment生命周期归actvity管理，现在改成fragment
class AdapterFragmentPager(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

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

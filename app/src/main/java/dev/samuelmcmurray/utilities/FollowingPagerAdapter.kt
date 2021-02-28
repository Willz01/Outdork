package dev.samuelmcmurray.utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class FollowingPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val listFragments: MutableList<Fragment> = ArrayList()
    private val listTitles: MutableList<String> = ArrayList()

    override fun getItemCount(): Int {
        return listTitles.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragments[position]
    }


    fun getPageTitle(position: Int): CharSequence? {
        return listTitles[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        listFragments.add(fragment)
        listTitles.add(title)
    }
}
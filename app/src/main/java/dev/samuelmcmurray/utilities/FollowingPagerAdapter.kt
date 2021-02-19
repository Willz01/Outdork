package dev.samuelmcmurray.utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.ArrayList

class FollowingPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    private val listFragments: MutableList<Fragment> = ArrayList()
    private val listTitles: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return listFragments[position]
    }

    override fun getCount(): Int {
        return listTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitles[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        listFragments.add(fragment)
        listTitles.add(title)
    }
}
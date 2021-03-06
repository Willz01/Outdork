package dev.samuelmcmurray.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 100
    private val ARG_OBJECT = "object"


    override fun createFragment(position: Int): Fragment {
        val fragment = OtherProfileFragment()
        fragment.arguments = Bundle().apply {

            putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }
}
package dev.samuelmcmurray.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentOtherProfileBinding
import dev.samuelmcmurray.ui.following.feeds.FollowingFeedsFragment

class OtherProfileFragment : Fragment() {

    companion object {
        fun newInstance() = OtherProfileFragment()
    }

    private lateinit var binding : FragmentOtherProfileBinding

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_other_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Tab " + position + 1
            viewPager.setCurrentItem(tab.position, true)
        }.attach()
//        viewPagerAdapter = ViewPagerAdapter(this)
//        viewPager = view.findViewById(R.id.view_pager)
//        viewPager.adapter = viewPagerAdapter
    }
}
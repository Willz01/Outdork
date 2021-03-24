package dev.samuelmcmurray.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentFollowingHolderBinding
import dev.samuelmcmurray.databinding.FragmentOtherProfileBinding
import dev.samuelmcmurray.ui.following.feeds.FollowingFeedsFragment
import dev.samuelmcmurray.ui.following.followinglist.FollowingListFragment
import dev.samuelmcmurray.utilities.FollowingPagerAdapter

class OtherProfileHolder : Fragment() {

    companion object {
        fun newInstance() = FollowingFeedsFragment()
    }

    private lateinit var binding : FragmentOtherProfileBinding

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager : ViewPager2

    private lateinit var followingPagerAdapter: FollowingPagerAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_other_profile, container, false)
        binding.lifecycleOwner = this

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        followingPagerAdapter = FollowingPagerAdapter(requireActivity())

        followingPagerAdapter.addFragment(OtherProfilePostsFragment(),"Posts")
        followingPagerAdapter.addFragment(OtherProfileInfoFragment(),"Bio")

        viewPager.adapter = followingPagerAdapter
        TabLayoutMediator(tabLayout, viewPager)  { currentTab, currentPosition ->
            currentTab.text = followingPagerAdapter.getPageTitle(currentPosition)
        }.attach()

        return binding.root
    }
}
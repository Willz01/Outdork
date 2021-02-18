package dev.samuelmcmurray.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentFollowingHolderBinding
import dev.samuelmcmurray.ui.following.feeds.FollowingFeedsFragment
import dev.samuelmcmurray.ui.following.followinglist.FollowingListFragment
import dev.samuelmcmurray.utilities.FollowingPagerAdapter


class FollowingHolder : Fragment() {

    companion object {
        fun newInstance() = FollowingFeedsFragment()
    }

    private lateinit var binding : FragmentFollowingHolderBinding

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager : ViewPager

    private lateinit var followingPagerAdapter: FollowingPagerAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_following_holder,container,false)
        binding.lifecycleOwner = this

        tabLayout = binding.tabLayout
        viewPager = binding.viewPager

        followingPagerAdapter = FollowingPagerAdapter(this.childFragmentManager)

        followingPagerAdapter.addFragment(FollowingFeedsFragment(),"Feeds")
        followingPagerAdapter.addFragment(FollowingListFragment(),"Following")

        viewPager.adapter = followingPagerAdapter
        tabLayout.setupWithViewPager(viewPager)


        return binding.root
    }




}
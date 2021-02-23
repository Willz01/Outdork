package dev.samuelmcmurray.ui.following.followinglist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentFollowingBinding
import dev.samuelmcmurray.databinding.FragmentFollowingListBinding
import dev.samuelmcmurray.ui.discoveries.DiscoveriesFragment


class FollowingListFragment : Fragment() {


    private lateinit var binding: FragmentFollowingListBinding

    companion object {
        fun newInstance() = FollowingListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_following_list, container, false)
        binding.lifecycleOwner = this


        return binding.root
    }

}
package dev.samuelmcmurray.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentProfileMenuBinding


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileMenuBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_menu, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }
/*
    fun addFragment(fragment: Fragment, string: String) {
        fragmentList.add(fragment)
        stringList.add(string)
    }
*/


}
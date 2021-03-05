package dev.samuelmcmurray.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentOtherProfileBinding

class ProfileUserPostsFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var binding: FragmentOtherProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_other_profile, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }
}
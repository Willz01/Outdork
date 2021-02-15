package dev.samuelmcmurray.ui.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentFollowingBinding
import dev.samuelmcmurray.utilities.InjectorUtils


class FollowingFragment : Fragment() {

    companion object {
        fun newInstance() = FollowingFragment()
    }
    private lateinit var binding : FragmentFollowingBinding
    private lateinit var viewModel : FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideFollowingViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(FollowingViewModel::class.java)


        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }
}
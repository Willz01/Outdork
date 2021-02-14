package dev.samuelmcmurray.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentAboutBinding
import dev.samuelmcmurray.utilities.InjectorUtils

class AboutFragment : Fragment() {

    companion object {
        fun newInstance() = AboutFragment()
    }
    private lateinit var binding : FragmentAboutBinding
    private lateinit var viewModel : AboutViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideAboutViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(AboutViewModel::class.java)


        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }

}
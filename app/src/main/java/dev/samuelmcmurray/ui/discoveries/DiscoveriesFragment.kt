package dev.samuelmcmurray.ui.discoveries

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.DiscoveriesFragmentBinding
import dev.samuelmcmurray.utilities.InjectorUtils


class DiscoveriesFragment : Fragment() {

    companion object {
        fun newInstance() = DiscoveriesFragment()
    }
    private lateinit var binding : DiscoveriesFragmentBinding
    private lateinit var viewModel : DiscoveriesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.discoveries_fragment, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideDiscoveriesViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(DiscoveriesViewModel::class.java)


        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }

}
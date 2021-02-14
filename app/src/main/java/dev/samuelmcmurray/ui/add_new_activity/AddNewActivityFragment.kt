package dev.samuelmcmurray.ui.add_new_activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentNewActivityBinding
import dev.samuelmcmurray.utilities.InjectorUtils

class AddNewActivityFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewActivityFragment()
    }

    private lateinit var binding : FragmentNewActivityBinding
    private lateinit var viewModelProvider : AddNewActivityViewModel
    private lateinit var viewModel: AddNewActivityViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideAddNewActivityViewModelFactory()
        viewModelProvider = ViewModelProvider(this, factory).get(AddNewActivityViewModel::class.java)
        viewModel = viewModelProvider
    }
}
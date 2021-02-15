package dev.samuelmcmurray.ui.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentReportBinding
import dev.samuelmcmurray.utilities.InjectorUtils

class ReportFragment : Fragment() {

    companion object {
        fun newInstance() = ReportFragment()
    }
    private lateinit var binding : FragmentReportBinding
    private lateinit var viewModel : ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideReportViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(ReportViewModel::class.java)


        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }

}
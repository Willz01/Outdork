package dev.samuelmcmurray.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        val view = binding.root


        val aboutText = view.findViewById<TextView>(R.id.aboutText)

        aboutText.text= """We are dorks about the adventures waiting for us outdoors.
            |This app allows you to make it easier for you to find the correct activity. 
            |
            |An active outdoor life is healthy for both heart, mind and body. No matter if you walk with your dog, looking for a good hunting place
            |or if you just want a walk in a nice forest - your active choice to be active is an investment in yourself. 
            |  
            |When the world is turned upside down, the wild life is the same, waiting for your next adventure!
            """.trimMargin()


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideAboutViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(AboutViewModel::class.java)


        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }

}
package dev.samuelmcmurray.ui.help

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentHelpBinding
import dev.samuelmcmurray.utilities.InjectorUtils


class HelpFragment : Fragment() {

    companion object {
        fun newInstance() = HelpFragment()
    }

    private lateinit var binding : FragmentHelpBinding
    private lateinit var viewModel : HelpViewModel
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val drawerLayout : DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_help, container, false)
        binding.setLifecycleOwner(this)
        // test
        navController = requireActivity().findNavController(R.id.fragment)
        bottomNavigationView = requireActivity().findViewById(R.id.nav)
        bottomNavigationView.visibility = View.GONE
        return binding.root
    }

    // testing out hiding the bottom nav bar in screen/fragments we don't need them -> works!
    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideHelpViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(HelpViewModel::class.java)


        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }
}
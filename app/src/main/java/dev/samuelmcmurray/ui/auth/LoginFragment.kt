package dev.samuelmcmurray.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentLoginBinding
import dev.samuelmcmurray.utilities.InjectorUtils

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)


        // set navigation bar invisible
        navController = requireActivity().findNavController(R.id.fragment)
        bottomNavigationView = requireActivity().findViewById(R.id.nav)
        bottomNavigationView.visibility = View.GONE
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideLoginViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)


        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }
}
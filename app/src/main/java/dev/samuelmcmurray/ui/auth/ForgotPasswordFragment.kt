package dev.samuelmcmurray.ui.auth

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil.inflate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }

    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, R.layout.fragment_forgot_password, container, false)
        binding.lifecycleOwner = this

        navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        val buttonCancel = binding.buttonCancel
        val buttonReset = binding.buttonReset

        buttonCancel.setOnClickListener {
            navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }

        buttonReset.setOnClickListener {
            val emailText = binding.editTextEmailAddress
            val email = emailText.text.toString().trim()
            resetPassword(email)
        }
    }

    fun resetPassword(email: String) {
        if (email.isNotBlank()) {
            viewModel.resetPassword(email)
            viewModel.resetEmailLiveData.observe(viewLifecycleOwner, Observer {
                val emailSent = it
                if (emailSent) {
                    val imm: InputMethodManager =
                        requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                    Toast.makeText(context, "Email sent", Toast.LENGTH_SHORT)
                        .show()
                    navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                } else {
                    Toast.makeText(context, "Make sure email is correct", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        } else {
            Toast.makeText(context, "Email must be entered", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
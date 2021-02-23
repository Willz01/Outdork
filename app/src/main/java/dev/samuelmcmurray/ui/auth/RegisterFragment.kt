package dev.samuelmcmurray.ui.auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentRegisterBinding

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    private lateinit var navHostFragment : NavHostFragment
    private lateinit var navController: NavController

    companion object {
        fun newInstance() = RegisterFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this

        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val signUpButton = binding.buttonSignUp
        val cancelButton = binding.buttonCancel
        val passwordText = binding.editTextPassword
        val passwordConfirmText = binding.editTextPassword2

        signUpButton.setOnClickListener {
            val firstNameText = binding.editTextFirstName
            val lastNameText = binding.editTextLastName
            val userNameText = binding.editTextUserName
            val emailText = binding.editTextEmailAddress
            val cityText = binding.editTextCity
            val stateText = binding.editTextState
            val countryText = binding.editTextCountry
            val passwordText = binding.editTextPassword
            val passwordConfirmText = binding.editTextPassword2

            val firstName = firstNameText.text.toString()
            val lastName = lastNameText.text.toString()
            val userName = userNameText.text.toString()
            val email = emailText.text.toString()
            val city = cityText.text.toString()
            val state = stateText.text.toString()
            val country = countryText.text.toString()
            val password = passwordText.text.toString()
            val passwordConfirm = passwordConfirmText.text.toString()

            register(firstName, lastName, userName, email, city, state, country, password, passwordConfirm)
        }

        cancelButton.setOnClickListener {
            navController.navigate(R.id.loginFragment)
        }

    }

    fun register(firstName: String, lastName: String, userName: String, email: String,
    city: String, state: String, country: String, password: String, passwordConfirm: String) {
        if (firstName.isNotBlank() && lastName.isNotBlank() && userName.isNotBlank() &&
                email.isNotBlank() && city.isNotBlank() && state.isNotBlank() && country.isNotBlank() &&
                password.isNotBlank() && passwordConfirm.isNotBlank() && (password == passwordConfirm)) {
            viewModel.register(
                firstName,
                lastName,
                userName,
                email,
                city,
                state,
                country,
                password,
                passwordConfirm
            )
            viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
                val fireBaseUser = it
                if (fireBaseUser != null) {
                    val imm: InputMethodManager =
                        requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
                Log.d(TAG, "login: " + fireBaseUser.uid)
            })
        } else {
            if (password != passwordConfirm) {
                Toast.makeText(context, "Passwords must match", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "All fields must be filled out", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
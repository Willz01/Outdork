package dev.samuelmcmurray.ui.auth

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CalendarView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseUser
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentRegisterBinding

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var calendarView: CalendarView
    private lateinit var userID : String
    private lateinit var user: FirebaseUser

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
        calendarView = binding.calendarView
        calendarView.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        val signUpButton = binding.buttonSignUp
        val cancelButton = binding.buttonCancel
        val dob = binding.editTextDOB

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
            val dob : Long = calendarView.date

            register(firstName, lastName, userName, email, city, state, country,
                password, passwordConfirm, dob)
            createUser(firstName, lastName, userName, email,
                city, state, country, dob)
            emailVerification()
            val imm: InputMethodManager =
                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        }

        cancelButton.setOnClickListener {
            navController.navigate(R.id.loginFragment)
        }

        dob.setOnClickListener {
            showHide(calendarView)
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val theMonth = month + 1
            dob.text = "$theMonth/$dayOfMonth/$year"
        }

    }

    private fun register(firstName: String, lastName: String, userName: String, email: String,
                         city: String, state: String, country: String, password: String, passwordConfirm: String, dob: Long) {
        if (firstName.isNotBlank() && lastName.isNotBlank() && userName.isNotBlank() &&
                email.isNotBlank() && city.isNotBlank() && state.isNotBlank() && country.isNotBlank() &&
                password.isNotBlank() && passwordConfirm.isNotBlank() && (password == passwordConfirm) &&
                dob > 0) {
            viewModel.register(email, password)
            viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
                val firebaseUser = it
                if (firebaseUser != null) {
                    user = firebaseUser
                    userID = user.uid
                    val imm: InputMethodManager =
                        requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
                Log.d(TAG, "login: " + firebaseUser.uid)
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

    private fun createUser(firstName: String, lastName: String, userName: String, email: String,
                           city: String, state: String, country: String, dob: Long){
        viewModel.createUser(firstName, lastName, userName, email,
            city, state, country, dob)
        viewModel.userCreatedLiveData.observe(viewLifecycleOwner, Observer {
            val userCreated = it
            if (userCreated) {
                val imm: InputMethodManager =
                    requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                Log.d(TAG, "UserCreated: " )
            }
        })
    }

    private fun emailVerification() {
        viewModel.emailVerification()
        viewModel.emailSentLiveData.observe(viewLifecycleOwner, Observer {
            val emailSent = it
            if (emailSent) {
                val imm: InputMethodManager =
                    requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                Log.d(TAG, "EmailSent: " )
            }
        })
    }

    private fun showHide(view: View) {
        if (view.visibility == View.GONE){
            view.visibility = View.VISIBLE
        } else{
            view.visibility = View.GONE
        }
    }
}
package dev.samuelmcmurray.ui.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareButton
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.repository.RegisterRepository
import dev.samuelmcmurray.databinding.FragmentLoginBinding
import dev.samuelmcmurray.ui.discoveries.DiscoveriesFragment
import org.json.JSONObject
import java.util.*
import kotlin.properties.Delegates


private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    // login handler variables
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton

    private var isUser = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this

        FacebookSdk.sdkInitialize(requireContext());
        AppEventsLogger.activateApp(requireActivity().application);

        navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController


        callbackManager = CallbackManager.Factory.create()
        loginButton = binding.loginButton
        loginButton.setPermissions(listOf("email", "public_profile", "user_gender"))
        loginButton.fragment = this
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);
        loginButton.fragment = this

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                // App code
                Log.d("LOGIN", "Login successful")
            }

            override fun onCancel() {
                // App code
                Log.d("LOGIN", "Login cancelled")
            }

            override fun onError(exception: FacebookException) {
                // App code
                Log.d("LOGIN", "Login failed")
            }
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val loginButton = binding.buttonLogin
        val registerText = binding.textViewSignUp
        val forgotPasswordText = binding.textViewForgotPassword

        loginButton.setOnClickListener {
            val editTextEmail = binding.editTextEmailAddress
            val editTextPassword = binding.editTextPassword

            val email: String = editTextEmail.text.toString().trim()
            val password: String = editTextPassword.text.toString()
            login(email, password)
        }

        registerText.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun login(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            viewModel.login(email, password)
            viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
                val fireBaseUser = it
                if (fireBaseUser != null) {
                    val imm: InputMethodManager =
                        requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
                Log.d(TAG, "login: $fireBaseUser ")
            })
        } else {
            Toast.makeText(context, "Email and Password must be entered", Toast.LENGTH_SHORT)
                .show()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        val graphRequest = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken(),
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
                    Log.d("LOGIN", `object`.toString())
                }
            })

        val bundle = Bundle()
        bundle.putString("fields", "gender, name, id, first_name, last_name")
        graphRequest.parameters = bundle

        graphRequest.executeAsync()
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Logout from device on access token change (User logged out)
     */
    object accessTokenTracker : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(
            oldAccessToken: AccessToken?,
            currentAccessToken: AccessToken?
        ) {
           if (currentAccessToken == null){
               LoginManager.getInstance().logOut()
           }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker.stopTracking()
    }
}
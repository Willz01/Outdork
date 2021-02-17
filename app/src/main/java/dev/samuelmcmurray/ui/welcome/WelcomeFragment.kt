package dev.samuelmcmurray.ui.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.inflate
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentWelcomeBinding
import dev.samuelmcmurray.ui.auth.LoginViewModel


private const val TAG = "WelcomeFragment"
class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, R.layout.fragment_welcome, container, false)
        binding.lifecycleOwner = this

        val progressBar = binding.progressBar
        progressBar.progress

        return binding.root
    }

}



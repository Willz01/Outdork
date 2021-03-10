package dev.samuelmcmurray.ui.profile.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.model.User
//import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.databinding.FragmentProfileMenuBinding


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileMenuBinding
    private lateinit var firstNameText: TextView
    private lateinit var lastNameText: TextView
    private lateinit var dobText: TextView
    private lateinit var emailText: TextView
    private lateinit var cityText: TextView
    private lateinit var stateText: TextView
    private lateinit var countryText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_menu, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstNameText = view.findViewById(R.id.firstNameText)
        lastNameText = view.findViewById(R.id.lastNameText)
        dobText = view.findViewById(R.id.dobText)
        emailText = view.findViewById(R.id.emailText)
        cityText = view.findViewById(R.id.cityText)
        stateText = view.findViewById(R.id.stateText)
        countryText = view.findViewById(R.id.countryText)
    }
/*
    fun onSaveClicked() {
        val newUserData = CurrentUser.copy(firstName = firstNameText.text.toString(),
            lastName = lastNameText.text.toString(), dob = dobText.text.toString(), email = emailText.text.toString(),
        city = cityText.text.toString(), state = stateText.text.toString(), country = countryText.text.toString())
        CurrentUserSingleton = newUserData
    }
    
 */
}
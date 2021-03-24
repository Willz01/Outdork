package dev.samuelmcmurray.ui.profile.user

//import dev.samuelmcmurray.data.singelton.CurrentUserSingleton

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import de.hdodenhof.circleimageview.CircleImageView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.databinding.FragmentProfileMenuBinding


class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileMenuBinding
    private lateinit var firstNameText: EditText
    private lateinit var lastNameText: EditText
    private lateinit var dobText: EditText
    private lateinit var emailText: EditText
    private lateinit var cityText: EditText
    private lateinit var stateText: EditText
    private lateinit var countryText: EditText
    private lateinit var profileImage: CircleImageView
    private val REQUEST_CODE = 1


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

        firstNameText.setText(CurrentUserSingleton.getInstance.currentUser!!.firstName)
        lastNameText.setText(CurrentUserSingleton.getInstance.currentUser!!.lastName)
        dobText.setText(CurrentUserSingleton.getInstance.currentUser!!.dob)
        emailText.setText(CurrentUserSingleton.getInstance.currentUser!!.email)
        cityText.setText(CurrentUserSingleton.getInstance.currentUser!!.city)
        stateText.setText(CurrentUserSingleton.getInstance.currentUser!!.state)
        countryText.setText(CurrentUserSingleton.getInstance.currentUser!!.country)

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            profileImage.setImageURI(uri)

        }


        profileImage = view.findViewById(R.id.profileImage)

        profileImage.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    fun onSaveClicked() {

        //id, firstName, lastName, userName, email, country, state, city, dob
        val newUserData = CurrentUser(
            id = CurrentUserSingleton.getInstance.currentUser!!.id,
            firstName = firstNameText.text.toString(),
            lastName = lastNameText.text.toString(),
            email = emailText.text.toString(),
            userName = CurrentUserSingleton.getInstance.currentUser!!.userName,
            country = countryText.text.toString(),
            state = stateText.text.toString(),
            city = cityText.text.toString(),
            dob = dobText.text.toString()
        )
//      Need to update the current user
    }

    fun pickImage(View: View?) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, REQUEST_CODE)
    }
}
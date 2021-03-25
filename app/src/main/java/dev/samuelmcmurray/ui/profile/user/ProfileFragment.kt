package dev.samuelmcmurray.ui.profile.user

//import dev.samuelmcmurray.data.singelton.CurrentUserSingleton

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.repository.ProfileRepository
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.databinding.FragmentProfileMenuBinding
import dev.samuelmcmurray.utilities.GlideApp
import java.io.ByteArrayOutputStream


private const val TAG = "ProfileFragment"

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var profileRepository: ProfileRepository
    private lateinit var binding: FragmentProfileMenuBinding
    private lateinit var firstNameText: EditText
    private lateinit var lastNameText: EditText
    private lateinit var dobText: TextView
    private lateinit var emailText: EditText
    private lateinit var cityText: EditText
    private lateinit var stateText: EditText
    private lateinit var countryText: EditText
    private lateinit var profileImage: CircleImageView
    private lateinit var profileImageURI: Uri
    private lateinit var application: Application
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstNameText = view.findViewById(R.id.firstNameText)
        lastNameText = view.findViewById(R.id.lastNameText)
        dobText = view.findViewById(R.id.dobText)
        emailText = view.findViewById(R.id.emailText)
        cityText = view.findViewById(R.id.cityText)
        stateText = view.findViewById(R.id.stateText)
        countryText = view.findViewById(R.id.countryText)
        profileImage = view.findViewById(R.id.profileImage)

        getCurrentUser()

        profileImage.setOnClickListener {
            updateProfileData()
        }
        firstNameText.setText(CurrentUserSingleton.getInstance.currentUser!!.firstName)
        lastNameText.setText(CurrentUserSingleton.getInstance.currentUser!!.lastName)
        dobText.setText(CurrentUserSingleton.getInstance.currentUser!!.dob)
        emailText.setText(CurrentUserSingleton.getInstance.currentUser!!.email)
        cityText.setText(CurrentUserSingleton.getInstance.currentUser!!.city)
        stateText.setText(CurrentUserSingleton.getInstance.currentUser!!.state)
        countryText.setText(CurrentUserSingleton.getInstance.currentUser!!.country)
        if (CurrentUserSingleton.getInstance.currentUser!!.hasImage) {
            context?.let {
                GlideApp.with(it.applicationContext)
                    .load(CurrentUserSingleton.getInstance.currentUser?.profilePhoto)
                    .into(profileImage)
            }
        } else {
            val defaultProfile = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        requireActivity().resources.getResourcePackageName(R.drawable.defaultprofile) + '/' +
                        requireActivity().resources.getResourceTypeName(R.drawable.defaultprofile) + '/' +
                        R.drawable.defaultprofile.toString()
            )
            context?.let {
                GlideApp.with(it.applicationContext).load(defaultProfile).into(profileImage)
            }
        }


        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                profileImage.setImageURI(uri)
                profileImageURI = uri!!
                CurrentUserSingleton.getInstance.currentUser!!.profilePhoto = uri.toString()
            }
        profileImage.setOnClickListener {
            getContent.launch("image/*")
            updateProfileImage()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentUser() {
        if (CurrentUserSingleton.getInstance.loggedIn || CurrentUserSingleton.getInstance.currentUser == null) {
            viewModel.getCurrentUser()
            viewModel.userLiveData.observe(viewLifecycleOwner) {
                val currentUser = it
                if (currentUser != null) {
                    Log.d(TAG, "currentUser success: ")
                } else {
                    Log.d(TAG, "getCurrentUser: failure")
                }
            }
        }
    }

    fun updateProfileData() {
        viewModel.updateProfileData(
            firstNameText.text.toString(), lastNameText.text.toString(), emailText.text.toString(),
            cityText.text.toString(), stateText.text.toString(), countryText.text.toString(),
            profileImageURI
        )
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            val currentUser = it
            if (currentUser != null) {
                Log.d(TAG, "currentUser success: ")
            } else {
                Log.d(TAG, "getCurrentUser: failure")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateProfileImage() {
        if (CurrentUserSingleton.getInstance.loggedIn || CurrentUserSingleton.getInstance.currentUser == null) {
            viewModel.updateProfileImage(profileImageURI)
            viewModel.userLiveData.observe(viewLifecycleOwner) {
                val currentUser = it
                if (currentUser != null) {
                    Log.d(TAG, "currentUser success: ")
                } else {
                    Log.d(TAG, "getCurrentUser: failure")
                }
            }
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }
}

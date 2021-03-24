package dev.samuelmcmurray.ui.welcome

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.databinding.FragmentWelcomeBinding


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigationView = requireActivity().findViewById(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        try {
            val imageResource = CurrentUserSingleton.getInstance.currentUser!!.profilePhoto
            val navPicture =
                headerView.findViewById<View>(R.id.profilePicture) as ImageView
            navPicture.setImageURI(imageResource)
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        try {
            val navUsername =
                headerView.findViewById<View>(R.id.profileName) as TextView
            navUsername.text = CurrentUserSingleton.getInstance.currentUser!!.userName
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        try {
            val navEmail =
                headerView.findViewById<View>(R.id.profileEmail) as TextView
            navEmail.text = CurrentUserSingleton.getInstance.currentUser!!.email
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    fun getCurrentUser() {
        val userRef = FirebaseFirestore.getInstance().collection("Users")
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        userRef.document(userID.toString()).get()
            .addOnSuccessListener { result ->
                val firstName = result["firstName"]
                val lastName = result["lastName"]
                val userName = result["userName"]
                val email = result["email"]
                val dateOfBirth = result["dateOfBirth"]
                val about = result["about"]
                val hasImage = result["hasImage"]
                val city = result["city"]
                val country = result["country"]
                val state = result["state"]
                Log.d(dev.samuelmcmurray.data.repository.TAG, "getCurrentUser: ${dateOfBirth.toString()}")
                val currentUser = CurrentUser(
                    userID.toString(), firstName.toString(), lastName.toString(),
                    userName.toString(), email.toString(), country.toString(), state.toString(),
                    city.toString(), dateOfBirth.toString()
                )

                Log.d(dev.samuelmcmurray.data.repository.TAG, "getCurrentUser: ${currentUser.age}")
                currentUser.about = about.toString()
                currentUser.hasImage = hasImage.toString().toBoolean()

                CurrentUserSingleton.getInstance.currentUser = currentUser
                userLiveData.postValue(currentUser)
                Log.d(dev.samuelmcmurray.data.repository.TAG, "getCurrentUser: DocumentSnapshot retrieved with ID: $userID")
            }
            .addOnFailureListener { e -> Log.w(dev.samuelmcmurray.data.repository.TAG, "Error adding document", e) }
    }
}






package dev.samuelmcmurray.data.repository

import android.app.Application
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.data.singelton.NewPostSingleton
import java.time.LocalDate
import java.time.Period
import java.util.*


private const val TAG = "ProfileRepository"
class ProfileRepository{
    private var application: Application
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    var postCreatedLiveData: MutableLiveData<Boolean>
    var storeImageLiveData: MutableLiveData<Boolean>
    var userLiveData: MutableLiveData<CurrentUser>
    private lateinit var firebaseAuth: FirebaseAuth
    private val firebaseApplication = FirebaseApp.getInstance()
    lateinit var userCreatedLiveData: MutableLiveData<Boolean>
    lateinit var emailSentLiveData: MutableLiveData<Boolean>


    constructor(application: Application) {
        this.application = application
        postCreatedLiveData = MutableLiveData()
        storeImageLiveData = MutableLiveData()
        userLiveData = MutableLiveData()
    }

    fun updateProfileImage(imageURI: Uri) {
        uploadToFireStorage(imageURI)

        var uid = CurrentUserSingleton.getInstance.currentUser!!.id
        val user = mapOf<String, Any?>(
            "firstName" to CurrentUserSingleton.getInstance.currentUser!!.firstName,
            "lastName" to CurrentUserSingleton.getInstance.currentUser!!.lastName,
            "userName" to CurrentUserSingleton.getInstance.currentUser!!.userName,
            "email" to CurrentUserSingleton.getInstance.currentUser!!.email,
            "country" to CurrentUserSingleton.getInstance.currentUser!!.country,
            "state" to CurrentUserSingleton.getInstance.currentUser!!.state,
            "city" to CurrentUserSingleton.getInstance.currentUser!!.city,
            "dateOfBirth" to CurrentUserSingleton.getInstance.currentUser!!.dob,
            "about" to CurrentUserSingleton.getInstance.currentUser!!.about,
            "hasImage" to true,
            "userImageURL" to CurrentUserSingleton.getInstance.currentUser!!.profilePhoto
        )
        Log.d(TAG, "updateImage: $user")


        val db = FirebaseFirestore.getInstance(firebaseApplication)
        db.collection("Users")
            .document(uid)
            .update(user)
            .addOnSuccessListener {
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: $uid"
                )
                userCreatedLiveData.postValue(true)
            }
            .addOnFailureListener { e -> Log.i(TAG, "Error adding document $e", e)
                userCreatedLiveData.postValue(false)}


    }

    fun uploadToFireStorage(userImage: Uri) {
        storage = FirebaseStorage.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
        val uid = CurrentUserSingleton.getInstance.currentUser!!.id
        val userprofileImage = storageRef!!.child("userImage/$uid" )
        val uploadTaskImage = userprofileImage.putFile(userImage)
        val urlTask = uploadTaskImage.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                Log.d("tag", "Failure: Uploaded Image URi is $userImage")
                Toast.makeText(
                    application.applicationContext,
                    "Upload Failed.",
                    Toast.LENGTH_SHORT
                ).show()
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation userprofileImage.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    application.applicationContext,
                    "User Is Updated.",
                    Toast.LENGTH_SHORT
                ).show()
                CurrentUserSingleton.getInstance.currentUser!!.profilePhoto = task.result.toString()
            }
        }
    }

    fun updateProfileData(
        firstName: String, lastName: String, email: String,
        city: String, state: String, country: String, ImageURI: Uri
    ) {
        var uid = CurrentUserSingleton.getInstance.currentUser!!.id
        storage = FirebaseStorage.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        val user = mapOf<String, Any>(
            "firstName" to firstName,
            "lastName" to lastName,
            "userName" to CurrentUserSingleton.getInstance.currentUser!!.userName,
            "email" to email,
            "country" to country,
            "state" to state,
            "city" to city,
            "dateOfBirth" to CurrentUserSingleton.getInstance.currentUser!!.dob,
            "about" to CurrentUserSingleton.getInstance.currentUser!!.about,
            "hasImage" to CurrentUserSingleton.getInstance.currentUser!!.hasImage,
            "userImageURL" to ImageURI.toString()
        )
        Log.d(TAG, "UpdateData: $user")

        val db = FirebaseFirestore.getInstance(firebaseApplication)
        db.collection("Users")
            .document("$uid")
            .update(user)
            .addOnSuccessListener {
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: $uid"
                )
                userCreatedLiveData.postValue(true)
            }
            .addOnFailureListener { e -> Log.i(TAG, "Error adding document $e", e)
                userCreatedLiveData.postValue(false)}


    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                val profileImage = result["userImageURL"]
                val city = result["city"]
                val country = result["country"]
                val state = result["state"]
                Log.d(TAG, "getCurrentUser: ${dateOfBirth.toString()}")
                val currentUser = CurrentUser(
                    userID.toString(), firstName.toString(), lastName.toString(),
                    userName.toString(), email.toString(), country.toString(), state.toString(),
                    city.toString(), dateOfBirth.toString()
                )

                currentUser.age = getAge(dateOfBirth.toString())
                Log.d(TAG, "getCurrentUser: ${currentUser.age}")
                currentUser.about = about.toString()
                currentUser.hasImage = hasImage.toString().toBoolean()

                CurrentUserSingleton.getInstance.currentUser = currentUser
                userLiveData.postValue(currentUser)
                Log.d(TAG, "getCurrentUser: DocumentSnapshot retrieved with ID: $userID")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAge(dob: String) : Int {
        val parts = dob.split("-")
        val year = parts[2]
        val day = parts[1]
        val month = parts[0]
        return Period.between(
            LocalDate.of(year.toInt(), month.toInt(), day.toInt()),
            LocalDate.now()
        ).years
    }
}
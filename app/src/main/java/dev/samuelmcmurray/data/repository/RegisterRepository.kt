package dev.samuelmcmurray.data.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.data.singelton.MutexLock
import kotlinx.coroutines.delay



private const val TAG = "RegisterRepository"

class RegisterRepository {
    private var application: Application
    private var firebaseAuth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    //var storage = FirebaseStorage.getInstance("gs://outdork-788e0.appspot.com")
    var storeImageLiveData: MutableLiveData<Boolean>
    var userLiveData: MutableLiveData<FirebaseUser>
    var loggedOutLiveData: MutableLiveData<Boolean>
    var userCreatedLiveData: MutableLiveData<Boolean>
    var emailSentLiveData: MutableLiveData<Boolean>

    constructor(application: Application) {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()
        userLiveData = MutableLiveData()
        loggedOutLiveData = MutableLiveData()
        userCreatedLiveData = MutableLiveData()
        emailSentLiveData = MutableLiveData()
        storeImageLiveData = MutableLiveData()

        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser);
            loggedOutLiveData.postValue(false);
        }
    }

    fun registerEmail(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                ContextCompat.getMainExecutor(application),
                { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        userLiveData.postValue(firebaseUser)
                        Log.d(TAG, "onComplete: " + firebaseUser?.uid)
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            "Registration Failure: " + (task.exception?.message
                                ?: "failed"),
                            Toast.LENGTH_SHORT
                        ).show()
                        loggedOutLiveData.postValue(true)
                    }
                })

    }

    suspend fun createUser(
        firstName: String, lastName: String, userName: String, email: String,
        city: String, state: String, country: String, dob: Long
    ) {
        Log.d(TAG, "createUser: Beginning")

        while ( MutexLock.getInstance.locked) {
            delay(250)
        }

        val uid = this.firebaseAuth.currentUser?.uid
        val currentUser = uid?.let {
            CurrentUser(
                it,
                firstName,
                lastName,
                userName,
                email,
                country,
                state,
                city,
                dob
            )}
        CurrentUserSingleton.getInstance.currentUser = currentUser
        Log.d(TAG, "createUser: Middle " + currentUser.toString())
        val user = hashMapOf(
            "userID" to currentUser?.id,
            "firstName" to currentUser?.firstName,
            "lastName" to currentUser?.lastName,
            "userName" to currentUser?.userName,
            "email" to currentUser?.email,
            "hasImage" to currentUser?.hasImage,
            "country" to currentUser?.country,
            "state" to currentUser?.state,
            "city" to currentUser?.city,
            "dateOfBirth" to currentUser?.dob,
            "about" to CurrentUserSingleton.getInstance.currentUser!!.about,
            "activities" to listOf(CurrentUserSingleton.getInstance.currentUser!!.activities)
        )
        Log.d(TAG, "createUser: Before set $user")
        db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .add(user)
            .addOnSuccessListener (ContextCompat.getMainExecutor(application), { _ ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: $uid"
                )
                userCreatedLiveData.postValue(true)
            })
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document ${e.localizedMessage}")
                userCreatedLiveData.postValue(false)}
    }

    suspend fun emailVerification() {

        while (MutexLock.getInstance.locked && MutexLock.getInstance.flag) {
            delay(500)
        }
        val user = firebaseAuth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener (ContextCompat.getMainExecutor(application), { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    emailSentLiveData.postValue(true)
                } else {
                    emailSentLiveData.postValue(false)
                }
            })
    }

//    private fun storeImage(uid: String, image: Uri) {
//        val storageReference = storage.reference
//        storageReference.child("UserPhotos/$uid/profileImage.jpg")
//        val uploadTask = storageReference.putFile(image)
//        uploadTask.addOnFailureListener {
//            storeImageLiveData.postValue(false)
//        }.addOnSuccessListener {
//            storeImageLiveData.postValue(true)
//        }
//    }

}
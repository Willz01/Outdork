package dev.samuelmcmurray.data.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton



private const val TAG = "RegisterRepository"

class RegisterRepository {
    private var application: Application
    private var firebaseAuth: FirebaseAuth
    private val firebaseApplication = FirebaseApp.getInstance()
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

    fun createUser(
        firstName: String, lastName: String, userName: String, email: String,
        city: String, state: String, country: String, dob: String
    ) {
        var uid = firebaseAuth.currentUser?.uid
        while (uid == null) {
            //TODO: fix so it cant go into an infinite loop
            uid = firebaseAuth.currentUser?.uid
        }

        val currentUser =
            CurrentUser(
                uid,
                firstName,
                lastName,
                userName,
                email,
                country,
                state,
                city,
                dob
            )

        val user = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "userName" to userName,
            "email" to email,
            "country" to country,
            "state" to state,
            "city" to city,
            "dateOfBirth" to dob,
            "about" to currentUser.about,
            "hasImage" to currentUser.hasImage
        )
        Log.d(TAG, "createUser: $user")

        val db = FirebaseFirestore.getInstance(firebaseApplication)
        db.collection("Users")
            .document("$uid")
            .set(user)
            .addOnSuccessListener {
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: $uid"
                )
                CurrentUserSingleton.getInstance.currentUser = currentUser
                userCreatedLiveData.postValue(true)
            }
            .addOnFailureListener { e -> Log.i(TAG, "Error adding document $e", e)
                userCreatedLiveData.postValue(false)}


    }

    fun emailVerification() {
        var user = firebaseAuth.currentUser
        while (user == null) {
            //TODO: fix so it cant go into an infinite loop
            user = firebaseAuth.currentUser
        }
        user.sendEmailVerification()
            .addOnCompleteListener (ContextCompat.getMainExecutor(application), { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    emailSentLiveData.postValue(true)
                } else {
                    emailSentLiveData.postValue(false)
                }
            })
    }

}
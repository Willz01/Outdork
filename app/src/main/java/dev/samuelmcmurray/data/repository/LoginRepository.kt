package dev.samuelmcmurray.data.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val TAG = "LoginRepository"
class LoginRepository {
    private var application: Application
    private var firebaseAuth: FirebaseAuth
    var userLiveData: MutableLiveData<FirebaseUser>
    var loggedOutLiveData: MutableLiveData<Boolean>

    private constructor(application: Application) {
        this.application = application
        this.firebaseAuth = FirebaseAuth.getInstance()
        this.userLiveData = MutableLiveData()
        this.loggedOutLiveData = MutableLiveData()

        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser);
            loggedOutLiveData.postValue(false);
        }
    }

    companion object {
        @Volatile private var instance : LoginRepository? = null

        /**
         * Creating a Singleton to pass information from database to
         * repository or to database from repository. So that we can move data to ViewModel
         * Repository handles most of the data
         */
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance ?: LoginRepository(application).also { instance = it }
            }
    }


    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                ContextCompat.getMainExecutor(application),
                { task ->
                    Log.d(TAG, "onComplete: ")
                    if (task.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        userLiveData.postValue(firebaseUser)
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            "Login Failure: " + (task.exception?.message
                                ?: "failed"),
                            Toast.LENGTH_SHORT
                        ).show()
                        loggedOutLiveData.postValue(true)
                    }
                })
    }

}
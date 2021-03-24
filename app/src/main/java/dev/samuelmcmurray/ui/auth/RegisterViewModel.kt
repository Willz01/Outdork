package dev.samuelmcmurray.ui.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import dev.samuelmcmurray.data.repository.RegisterRepository
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

private const val TAG = "RegisterViewModel"
class RegisterViewModel : AndroidViewModel{

    private var registerRepository : RegisterRepository
    private val myCoroutineScope = CoroutineScope(Dispatchers.IO)
    var userLiveData : MutableLiveData<FirebaseUser>
    var loggedOutLiveData : MutableLiveData<Boolean>
    var userCreatedLiveData: MutableLiveData<Boolean>
    var emailSentLiveData: MutableLiveData<Boolean>
    val channel = Channel<FirebaseUser>()

    constructor(application: Application) : super(application) {
        this.registerRepository = RegisterRepository(application)
        userLiveData = registerRepository.userLiveData
        loggedOutLiveData = registerRepository.loggedOutLiveData
        userCreatedLiveData = registerRepository.userCreatedLiveData
        emailSentLiveData = registerRepository.emailSentLiveData
    }

    fun register(firstName: String, lastName: String, userName: String, email: String,
                 city: String, state: String, country: String, password: String, dob: String) {

        myCoroutineScope.launch {
                try {
                    registerUser(email, password)
                } catch (e: Exception) {

                }

        }
        myCoroutineScope.launch {
            try {
                createUser(firstName, lastName, userName, email, city, state, country, dob)
            } catch(e: Exception) {
                Log.d(TAG, "register: $e")
            }
        }

        myCoroutineScope.launch {
            try {
                emailVerification()
            } catch(e: Exception) {
                Log.d(TAG, "register: $e")
            }
        }
    }

    private fun registerUser(email: String, password: String)  {

        registerRepository.registerEmail(email, password)
        CurrentUserSingleton.getInstance.firstTimeRegister = true
    }

    private suspend fun emailVerification() {
        delay(500)
        registerRepository.emailVerification()
    }

    private suspend fun createUser(firstName: String, lastName: String, userName: String, email: String,
                                   city: String, state: String, country: String, dob: String) {
        delay(500)
        registerRepository.createUser(firstName, lastName, userName, email, city, state, country, dob)
    }
}


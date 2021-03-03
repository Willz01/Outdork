package dev.samuelmcmurray.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dev.samuelmcmurray.data.repository.RegisterRepository
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.data.singelton.MutexLock
import kotlinx.coroutines.launch

class RegisterViewModel : AndroidViewModel {

    private var registerRepository: RegisterRepository
    var userLiveData: MutableLiveData<FirebaseUser>
    var loggedOutLiveData: MutableLiveData<Boolean>
    var userCreatedLiveData: MutableLiveData<Boolean>
    var emailSentLiveData: MutableLiveData<Boolean>

    constructor(application: Application) : super(application) {
        this.registerRepository = RegisterRepository(application)
        userLiveData = registerRepository.userLiveData
        loggedOutLiveData = registerRepository.loggedOutLiveData
        userCreatedLiveData = registerRepository.userCreatedLiveData
        emailSentLiveData = registerRepository.emailSentLiveData
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                CurrentUserSingleton.getInstance.firstTimeRegister = true
                registerRepository.registerEmail(email, password)
            } catch (e: Exception) {

            }
        }
    }

    fun emailVerification() {
        viewModelScope.launch {
            try {
                registerRepository.emailVerification()
            } catch (e: Exception) {

            }
        }
    }

    fun createUser(
        firstName: String, lastName: String, userName: String, email: String,
        city: String, state: String, country: String, dob: Long
    ) {
        viewModelScope.launch {
            try {
                registerRepository.createUser(
                    firstName,
                    lastName,
                    userName,
                    email,
                    city,
                    state,
                    country,
                    dob
                )
            } catch (e: Exception) {

            } finally {
                MutexLock.getInstance.flag = false
            }
        }
    }
}


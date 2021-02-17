package dev.samuelmcmurray.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dev.samuelmcmurray.data.repository.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel : AndroidViewModel{

    private var registerRepository : RegisterRepository
    var userLiveData : MutableLiveData<FirebaseUser>
    var loggedOutLiveData : MutableLiveData<Boolean>

    constructor(application: Application) : super(application) {
        this.registerRepository = RegisterRepository(application)
        userLiveData = registerRepository.userLiveData
        loggedOutLiveData = registerRepository.loggedOutLiveData
    }

    fun register(firstName: String, lastName: String, userName: String, email: String,
                 city: String, state: String, country: String, password: String, passwordConfirm: String)  {
        viewModelScope.launch {
            try {
                registerRepository.registerEmail(email, password)
            } catch (e : Exception) {

            }
        }
    }
}
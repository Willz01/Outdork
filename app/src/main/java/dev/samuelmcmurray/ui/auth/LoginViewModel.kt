package dev.samuelmcmurray.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dev.samuelmcmurray.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel : AndroidViewModel{
    private var loginRepository : LoginRepository
    var userLiveData : MutableLiveData<FirebaseUser>
    var loggedOutLiveData : MutableLiveData<Boolean>

    constructor(application: Application) : super(application) {
        loginRepository = LoginRepository(application)
        userLiveData = loginRepository.userLiveData
        loggedOutLiveData = loginRepository.loggedOutLiveData
    }

    fun login(email : String, password : String)  {
        viewModelScope.launch {
            try {
                loginRepository.login(email, password)
            } catch (e : Exception) {

            }
        }
    }
}
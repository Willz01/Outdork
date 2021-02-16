package dev.samuelmcmurray.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.LoginRepository

class LoginViewModelFactory(private val loginRepository: LoginRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel() as T
    }
}
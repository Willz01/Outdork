package dev.samuelmcmurray.ui.profile.user

import android.app.Application
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.repository.ProfileRepository
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import kotlinx.coroutines.launch

private const val TAG = "ProfileViewModel"
class ProfileViewModel : AndroidViewModel {

    private lateinit var profileRepository : ProfileRepository
    var userLiveData: MutableLiveData<CurrentUser>
    private val filePath: MutableLiveData<Uri>

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentUser()  {
        if (CurrentUserSingleton.getInstance.loggedIn || CurrentUserSingleton.getInstance.currentUser == null) {
            viewModelScope.launch {
                try {
                    profileRepository.getCurrentUser()
                } catch (e: Exception) {
                    Log.d(TAG, "getCurrentUser: $e")
                }
            }
        }
    }

    fun updateProfileImage() {
        CurrentUserSingleton.getInstance.currentUser.profilePhoto = url
    }

    constructor(application: Application) : super(application) {
        profileRepository = ProfileRepository(application)
        userLiveData = profileRepository.userLiveData
        filePath = MutableLiveData()
    }
}
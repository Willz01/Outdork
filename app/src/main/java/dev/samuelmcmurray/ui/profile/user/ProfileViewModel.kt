package dev.samuelmcmurray.ui.profile.user

import android.app.Application
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.repository.ProfileRepository
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ProfileViewModel"
class ProfileViewModel : AndroidViewModel {

    private var profileRepository : ProfileRepository
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateProfileImage(imageURI: Uri) {
        Log.d(TAG, "profileImage")
        CurrentUserSingleton.getInstance.currentUser?.hasImage = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                profileRepository.updateProfileImage(imageURI)
            } catch (e: Exception) {
                Log.d(dev.samuelmcmurray.ui.profile.user.TAG, "updateUserImage: $e")
            }
        }
    }

    fun updateProfileData(firstName: String, lastName: String, email: String,
                          city: String, state: String, country: String, ImageURI: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                profileRepository.updateProfileData(firstName, lastName, email, city, state, country, ImageURI)
            } catch (e: Exception) {
                Log.d(dev.samuelmcmurray.ui.profile.user.TAG, "updateUserData: $e")
            }
        }
    }

    constructor(application: Application) : super(application) {
        profileRepository = ProfileRepository(application)
        userLiveData = profileRepository.userLiveData
        filePath = MutableLiveData()
    }
}
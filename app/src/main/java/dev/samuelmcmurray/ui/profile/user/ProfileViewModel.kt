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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateProfileImage(url: String) {
        CurrentUserSingleton.getInstance.currentUser!!.profilePhoto = url
    }
    fun newPost(message: String) {
        var hasImage : Boolean = false
        val likes = 0
        var imageUri = Uri.EMPTY
        val comments : List<String> = emptyList()
        if (filePath.value != null) {
            hasImage = true
            imageUri = filePath.value
        }
        Log.d(dev.samuelmcmurray.ui.profile.TAG, "newPost: $message $likes $hasImage")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                profileRepository.updateProfileImage(imageUri)
            } catch (e: Exception) {
                Log.d(dev.samuelmcmurray.ui.profile.TAG, "newPost: $e")
            }
        }
    }

    fun updateProfileData() {

    }

    constructor(application: Application) : super(application) {
        profileRepository = ProfileRepository(application)
        userLiveData = profileRepository.userLiveData
        filePath = MutableLiveData()
    }
}
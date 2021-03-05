package dev.samuelmcmurray.ui.discoveries

import android.app.Application
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.repository.DiscoveriesRepository
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

private const val TAG = "DiscoveriesViewModel"
class DiscoveriesViewModel : AndroidViewModel {
    private var discoveriesRepository : DiscoveriesRepository
    private var imageLiveData = MutableLiveData<Uri>()
    private val newPostVisibilityLiveData = MutableLiveData<Boolean>()
    var postCreatedLiveData: MutableLiveData<Boolean>
    var userLiveData: MutableLiveData<CurrentUser>
    val hideBoolean: LiveData<Boolean> get() = newPostVisibilityLiveData
    var image: Uri = Uri.EMPTY

    constructor(application: Application) : super(application) {
        discoveriesRepository = DiscoveriesRepository(application)
        postCreatedLiveData = discoveriesRepository.postCreatedLiveData
        userLiveData = discoveriesRepository.userLiveData
    }

    private fun getImage(): LiveData<Uri> = imageLiveData

    fun onGetImage(result: Uri) {
        imageLiveData.postValue(result)
        image = result
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun newPost(message: String) {
        var hasImage : Boolean = false
        val likes = 0
        val comments : List<String> = emptyList()
//        if (image != Uri.EMPTY || image != null) {
//            hasImage = true
//        }
        Log.d(TAG, "newPost: $message $likes $hasImage")
        CoroutineScope(Default).launch {
            try {
                discoveriesRepository.newPost(message, hasImage, likes)
            } catch (e: Exception) {
                Log.d(TAG, "newPost: $e")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentUser()  {
        if (CurrentUserSingleton.getInstance.loggedIn || CurrentUserSingleton.getInstance.currentUser == null) {
            viewModelScope.launch {
                try {
                    discoveriesRepository.getCurrentUser()
                } catch (e: Exception) {
                    Log.d(TAG, "getCurrentUser: $e")
                }
            }
        }
    }

    fun hideNewPostFragment(value: Boolean) {
        newPostVisibilityLiveData.postValue(value)
    }

}

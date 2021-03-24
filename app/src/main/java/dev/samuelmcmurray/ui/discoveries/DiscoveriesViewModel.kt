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
import dev.samuelmcmurray.data.model.Post
import dev.samuelmcmurray.data.repository.DiscoveriesRepository
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "DiscoveriesViewModel"
class DiscoveriesViewModel : AndroidViewModel {
    private var discoveriesRepository : DiscoveriesRepository

    private val newPostVisibilityLiveData = MutableLiveData<Boolean>()
    var postCreatedLiveData: MutableLiveData<Boolean>
    var userLiveData: MutableLiveData<CurrentUser>
    val hideBoolean: LiveData<Boolean> get() = newPostVisibilityLiveData
    var postsListLiveData: MutableLiveData<List<Post>>
    var downloadURLLiveData: MutableLiveData<Boolean>
    private val filePath: MutableLiveData<Uri>

    constructor(application: Application) : super(application) {
        discoveriesRepository = DiscoveriesRepository(application)
        postCreatedLiveData = discoveriesRepository.postCreatedLiveData
        userLiveData = discoveriesRepository.userLiveData
        postsListLiveData = discoveriesRepository.postsListLiveData
        downloadURLLiveData = discoveriesRepository.downloadURLLiveData
        filePath = MutableLiveData()
    }

    fun onGetImage(path: Uri) {
        filePath.value = path
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun newPost(message: String) {
        var hasImage = false
        val likes = 0
        var imageUri = Uri.EMPTY
        if (filePath.value != null) {
            hasImage = true
            imageUri = filePath.value
        }
        Log.d(TAG, "newPost: $message $likes $hasImage")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                discoveriesRepository.newPost(message, imageUri, hasImage, likes)
            } catch (e: Exception) {
                Log.d(TAG, "newPost: $e")
            }
        }
    }

    fun saveImage(){
        if (filePath.value != null) {
            var imageUri = filePath.value
            CoroutineScope(Dispatchers.IO).launch {
                if (imageUri != Uri.EMPTY) {
                    discoveriesRepository.uploadImageToFirebase(imageUri!!)
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentUser()  {
        if (CurrentUserSingleton.getInstance.loggedIn || CurrentUserSingleton.getInstance.currentUser == null) {
            CoroutineScope(Dispatchers.IO).launch{
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPostsList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                discoveriesRepository.getPostsList()
            }catch (exception: Exception) {
                Log.d(TAG, "getPostsList: $exception")
            }
        }
    }

    fun updateLikes(uid: String, postID: String, likes: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                discoveriesRepository.updateLikes(uid, postID, likes)
            } catch (e: Exception) {
                Log.d(TAG, "updateLikes: $e")
            }
        }
    }
}
package dev.samuelmcmurray.ui.profile

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dev.samuelmcmurray.data.model.Post
import dev.samuelmcmurray.data.repository.OtherProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "OtherProfileViewModel"
class OtherProfileViewModel: AndroidViewModel {
    private var otherProfileRepository: OtherProfileRepository
    var postsListLiveData: MutableLiveData<List<Post>>

    constructor(application: Application): super(application) {
        otherProfileRepository = OtherProfileRepository(application)
        postsListLiveData = otherProfileRepository.postsListLiveData
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPostsList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                otherProfileRepository.getPostsList()
            }catch (exception: Exception) {
                Log.d(TAG, "getPostsList: $exception")
            }
        }
    }

    fun updateLikes(uid: String, postID: String, likes: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                otherProfileRepository.updateLikes(uid, postID, likes)
            } catch (e: Exception) {
                Log.d(TAG, "updateLikes: $e")
            }
        }
    }
}
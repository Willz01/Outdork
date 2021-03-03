package dev.samuelmcmurray.ui.discoveries

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.repository.DiscoveriesRepository
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import kotlinx.coroutines.launch


class DiscoveriesViewModel : AndroidViewModel {
    private var discoveriesRepository : DiscoveriesRepository
    private var imageLiveData = MutableLiveData<Uri>()
    var newPostVisibilityLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private var postCreatedLiveData: MutableLiveData<Boolean>
    var userLiveData: MutableLiveData<CurrentUser>

    constructor(application: Application) : super(application) {
        discoveriesRepository = DiscoveriesRepository(application)
        postCreatedLiveData = discoveriesRepository.postCreatedLiveData
        userLiveData = discoveriesRepository.userLiveData
    }

    fun getImage(): LiveData<Uri> = imageLiveData

    fun onGetImage(result: Uri) {
        imageLiveData.value = result
    }

    fun newPost(message: String) {
        var hasImage : Boolean = false
        var image: Uri = Uri.EMPTY
        if (getImage().value != Uri.EMPTY) {
            hasImage = true
            image = getImage().value!!
        }
        val likes = 0
        val comments : List<String> = listOf()
    }

    fun getCurrentUser()  {
        if (CurrentUserSingleton.getInstance.loggedIn) {
            viewModelScope.launch {
                try {
                    discoveriesRepository.getCurrentUser()
                } catch (e: Exception) {

                }
            }
        }
    }
}
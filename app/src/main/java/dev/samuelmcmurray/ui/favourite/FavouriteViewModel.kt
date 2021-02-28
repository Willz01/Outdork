package dev.samuelmcmurray.ui.favourite

import android.app.Application
import android.app.Service
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dev.samuelmcmurray.data.database.FollowingDatabase
import dev.samuelmcmurray.data.repository.FavouritesRepository
import dev.samuelmcmurray.ui.post.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {

    val readAllFavourites: LiveData<List<Post>>
    private val repository: FavouritesRepository



    init {
        val followingDao = FollowingDatabase.getDatabase(application).bookmarkDao()
        repository = FavouritesRepository(followingDao)
        readAllFavourites = repository.readAllFavourites
    }

    fun addPost(post: Post) {
        viewModelScope.launch {
            repository.addPost(post)
        }
    }

    fun removePost(post: Post) {
        viewModelScope.launch {
            repository.removePost(post)
        }
    }




}
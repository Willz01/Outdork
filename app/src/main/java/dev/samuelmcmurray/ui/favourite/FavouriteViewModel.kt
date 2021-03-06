package dev.samuelmcmurray.ui.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dev.samuelmcmurray.data.database.FavoriteDatabase
import dev.samuelmcmurray.data.repository.FavouritesRepository
import dev.samuelmcmurray.ui.post.Post
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {

    val readAllFavourites: LiveData<List<Post>>
    private val repository: FavouritesRepository



    init {
        val followingDao = FavoriteDatabase.getDatabase(application).bookmarkDao()
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
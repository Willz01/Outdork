package dev.samuelmcmurray.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dev.samuelmcmurray.data.database.FavoriteDatabase
import dev.samuelmcmurray.data.repository.FavoritesRepository
import dev.samuelmcmurray.ui.post.Post
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    val readAllFavorites: LiveData<List<Post>>
    val readAllPost: List<Post>
    private val repository: FavoritesRepository


    init {
        val followingDao = FavoriteDatabase.getDatabase(application).bookmarkDao()
        repository = FavoritesRepository(followingDao)
        readAllFavorites = repository.readAllFavorites
        readAllPost = repository.readAllPost
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
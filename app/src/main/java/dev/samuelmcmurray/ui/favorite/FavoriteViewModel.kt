package dev.samuelmcmurray.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dev.samuelmcmurray.data.database.FavoriteDatabase
import dev.samuelmcmurray.data.repository.FavoritesRepository
import dev.samuelmcmurray.ui.post.PostLocal
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    val readAllFavorites: LiveData<List<PostLocal>>
    val readAllPost: List<PostLocal>
    private val repository: FavoritesRepository


    init {
        val followingDao = FavoriteDatabase.getDatabase(application).bookmarkDao()
        repository = FavoritesRepository(followingDao)
        readAllFavorites = repository.readAllFavorites
        readAllPost = repository.readAllPost
    }

    fun addPost(post: PostLocal) {
        viewModelScope.launch {
            repository.addPost(post)
        }
    }

    fun removePost(post: PostLocal) {
        viewModelScope.launch {
            repository.removePost(post)
        }
    }

}
package dev.samuelmcmurray.ui.like

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import dev.samuelmcmurray.data.database.LikesDatabase
import dev.samuelmcmurray.data.repository.LikesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikeViewModel(application: Application): AndroidViewModel(application) {
    val readAllLikes: LiveData<List<Like>>
    val readAllLike: List<Like>
    private val repository: LikesRepository

    init {
        val likesDAO = LikesDatabase.getDatabase(application).likesDAO()
        repository = LikesRepository(likesDAO)
        readAllLikes = repository.readAllLikes
        readAllLike = repository.readAllLike
    }

    fun addLike(like: Like) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.addLike(like)
        }
    }

    fun removeLike(like: Like) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.removeLike(like.postID)
        }
    }
}
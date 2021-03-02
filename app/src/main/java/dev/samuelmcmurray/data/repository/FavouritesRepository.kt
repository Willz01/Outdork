package dev.samuelmcmurray.data.repository

import androidx.lifecycle.LiveData
import dev.samuelmcmurray.data.dao.FavoriteDao
import dev.samuelmcmurray.ui.post.Post

class FavouritesRepository(private val favoriteDao: FavoriteDao) {

    val readAllFavourites: LiveData<List<Post>> = favoriteDao.readAllFavourites()

    suspend fun addPost(post: Post) {
        favoriteDao.add(post)
    }

    suspend fun removePost(post: Post) {
        favoriteDao.removePost(post.postID)
    }

}
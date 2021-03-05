package dev.samuelmcmurray.data.repository

import androidx.lifecycle.LiveData
import dev.samuelmcmurray.data.dao.FavoriteDao
import dev.samuelmcmurray.ui.post.Post

class FavoritesRepository(private val favoriteDao: FavoriteDao) {

    val readAllFavorites: LiveData<List<Post>> = favoriteDao.readAllFavorites()

    val readAllPost : List<Post> = favoriteDao.getPosts()

    suspend fun addPost(post: Post) {
        favoriteDao.add(post)
    }

    suspend fun removePost(post: Post) {
        favoriteDao.removePost(post.postID)
    }

}
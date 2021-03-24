package dev.samuelmcmurray.data.repository

import androidx.lifecycle.LiveData
import dev.samuelmcmurray.data.dao.FavoriteDao
import dev.samuelmcmurray.ui.post.PostLocal

class FavoritesRepository(private val favoriteDao: FavoriteDao) {

    val readAllFavorites: LiveData<List<PostLocal>> = favoriteDao.readAllFavorites()

    val readAllPost : List<PostLocal> = favoriteDao.getPosts()

    suspend fun addPost(post: PostLocal) {
        favoriteDao.add(post)
    }

    suspend fun removePost(post: PostLocal) {
        favoriteDao.removePost(post.postID)
    }

}
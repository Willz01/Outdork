package dev.samuelmcmurray.data.repository

import androidx.lifecycle.LiveData
import dev.samuelmcmurray.data.dao.FollowingDao
import dev.samuelmcmurray.ui.post.Post

class FavouritesRepository(private val followingDao: FollowingDao){

    val readAllFavourites : LiveData<List<Post>> = followingDao.readAllFavourites()

    suspend fun addPost(post: Post){
        followingDao.add(post)
    }

}
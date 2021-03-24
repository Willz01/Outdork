package dev.samuelmcmurray.data.repository

import androidx.lifecycle.LiveData
import dev.samuelmcmurray.data.dao.LikesDAO
import dev.samuelmcmurray.ui.like.Like

class LikesRepository(private val likesDAO: LikesDAO) {

    val readAllLikes: LiveData<List<Like>>  = likesDAO.readAllLikes()


    val readAllLike: List<Like> = likesDAO.getLikes()

    suspend fun addLike(postID: Like){
        likesDAO.add(postID)
    }

    suspend fun removeLike(postID: String){
        likesDAO.removeLike(postID)
    }
}
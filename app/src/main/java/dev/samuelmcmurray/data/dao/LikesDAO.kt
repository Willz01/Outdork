package dev.samuelmcmurray.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.samuelmcmurray.ui.like.Like

@Dao
interface LikesDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(like: Like)

    @Ignore
    @Query("select * from likes_table ")
    fun readAllLikes(): LiveData<List<Like>>


    @Query("Delete from likes_table where postID = :postID")
    suspend fun removeLike(postID: String)

    @Query("Select * from likes_table")
    fun getLikes() : List<Like>
}
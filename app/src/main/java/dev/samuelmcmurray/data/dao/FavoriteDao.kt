package dev.samuelmcmurray.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.samuelmcmurray.ui.post.PostLocal

@Dao
interface FavoriteDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(post: PostLocal)

    @Ignore
    @Query("select * from favorite_table order by post_id asc")
    fun readAllFavorites(): LiveData<List<PostLocal>>


    @Query("Delete from favorite_table where postID = :postID")
    suspend fun removePost(postID: String)

    @Query("Select * from favorite_table order by post_id asc")
    fun getPosts() : List<PostLocal>

}
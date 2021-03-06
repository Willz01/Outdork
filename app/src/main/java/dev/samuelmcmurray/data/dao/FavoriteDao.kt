package dev.samuelmcmurray.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.samuelmcmurray.ui.post.Post

@Dao
interface FavoriteDao {

    /**
     * TODO
     * On duplicate abort, don't add to db
     * hmm - didn't work as expected, have an idea, will fix later
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(post: Post)

    @Ignore
    @Query("select * from favourite_table order by post_id asc")
    fun readAllFavourites(): LiveData<List<Post>>


    @Query("Delete from favourite_table where postID = :postID")
    suspend fun removePost(postID: String)

}
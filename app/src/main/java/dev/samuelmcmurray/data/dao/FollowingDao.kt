package dev.samuelmcmurray.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import dev.samuelmcmurray.ui.post.Post

@Dao
interface FollowingDao {

    /**
     * TODO
     * On duplicate abort, don't add to db
     * hmm - didn't work as expected, have an idea, will fix later
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(post: Post)

    @Ignore
    @Query("select * from favourite_table order by post_id asc")
    fun readAllFavourites(): LiveData<List<Post>>

    @Delete
    fun remove(post: Post)
}
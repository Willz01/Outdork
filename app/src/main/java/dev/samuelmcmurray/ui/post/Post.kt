package dev.samuelmcmurray.ui.post

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Post class is used in bookmarks
 */

@Entity(tableName = "favourite_table")
data class Post(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "post_id") val id: Int = 0,
    val profilePicture: Int,
    val image_post: Int,
    var rating: Double,
    val poster: String,
    val date: String,
    val content: String
) {

}
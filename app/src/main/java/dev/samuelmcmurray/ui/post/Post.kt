package dev.samuelmcmurray.ui.post

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Post class is used in favourites
 * The id field can always be 0 since it will still be overwritten in the database because it's auto increment
 * Another field postID exist to have only distinct post in the favourite database
 */

@Entity(tableName = "favorite_table", indices = [Index(value = ["postID"], unique = true)])
data class Post(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "post_id")
    val id: Int = 0,
    val postID: String,
    val profilePicture: Int,
    val image_post: Int,
    var rating: Double,
    val poster: String,
    val date: String,
    val content: String
)
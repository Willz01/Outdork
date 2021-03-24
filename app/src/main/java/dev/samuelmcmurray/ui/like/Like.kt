package dev.samuelmcmurray.ui.like

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "likes_table", indices = [Index(value = ["post_ID"], unique = true)])
data class Like(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "post_ID")
    val likeId: Long = 0L,
    val postID: String
) {


}
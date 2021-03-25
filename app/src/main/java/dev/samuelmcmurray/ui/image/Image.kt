package dev.samuelmcmurray.ui.image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "image", indices = [Index(value = ["image_id"], unique = true)])
data class Image(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "image_id")
    val id : Int = 0,
    @ColumnInfo(name = "isPost")
    val post: Boolean,
    @ColumnInfo(name = "full_id")
    val fullId: String,
    @ColumnInfo(name = "imageList")
    val image: String? = null   ) {

}
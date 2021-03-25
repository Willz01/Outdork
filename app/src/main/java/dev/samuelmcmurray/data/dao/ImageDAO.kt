package dev.samuelmcmurray.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.samuelmcmurray.ui.image.Image

@Dao
interface ImageDAO {
    @Insert
    fun insert(vararg images: Image?)

    @Query("SELECT * FROM Image")
    fun getAllImage(): List<Image?>?

    @Query("SELECT * FROM Image where image_id = :imageId")
    fun getImageByImageId(imageId: Int): Image?

    @Query("SELECT * FROM Image where full_id = :fullId")
    fun getImageByFullId(fullId: String): Image?

    @Query("DELETE FROM Image where full_id = :fullId")
    fun deleteImageByFullId(fullId: String)

    @Query("DELETE FROM Image where image_id = :imageId")
    fun deleteImageById(imageId: Int)

}
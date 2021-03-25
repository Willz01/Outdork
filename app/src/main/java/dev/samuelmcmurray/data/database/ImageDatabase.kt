package dev.samuelmcmurray.data.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.samuelmcmurray.data.dao.ImageDAO
import dev.samuelmcmurray.ui.image.Image
import dev.samuelmcmurray.ui.like.Like
import dev.samuelmcmurray.utilities.ImageBitmapString


@Database(entities = [Image::class], version = 1, exportSchema = false)
@TypeConverters(ImageBitmapString::class)
abstract class ImageDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDAO?

    companion object {
        @Volatile
        private var INSTANCE: ImageDatabase? = null

        fun getDatabase(context: Context): ImageDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageDatabase::class.java,
                    "image_table"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
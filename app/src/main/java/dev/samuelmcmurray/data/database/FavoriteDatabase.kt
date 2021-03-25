package dev.samuelmcmurray.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.samuelmcmurray.data.dao.FavoriteDao
import dev.samuelmcmurray.ui.post.PostLocal

@Database(entities = [PostLocal::class], version = 6, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): FavoriteDao


    companion object {
        @Volatile
        private var INSTANCE: FavoriteDatabase? = null

        fun getDatabase(context: Context): FavoriteDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDatabase::class.java,
                    "favorite_database"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
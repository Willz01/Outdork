package dev.samuelmcmurray.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.samuelmcmurray.data.dao.FollowingDao
import dev.samuelmcmurray.ui.post.Post

@Database(entities = [Post::class], version = 2, exportSchema = false)
abstract class FollowingDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): FollowingDao


    companion object {
        @Volatile
        private var INSTANCE: FollowingDatabase? = null

        fun getDatabase(context: Context): FollowingDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FollowingDatabase::class.java,
                    "favourite_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
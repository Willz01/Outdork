package dev.samuelmcmurray.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.samuelmcmurray.data.dao.LikesDAO
import dev.samuelmcmurray.ui.like.Like

@Database(entities = [Like::class],version = 5, exportSchema = false)
abstract class LikesDatabase : RoomDatabase(){

    abstract fun likesDAO(): LikesDAO

    companion object {
        @Volatile
        private var INSTANCE: LikesDatabase? = null

        fun getDatabase(context: Context): LikesDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LikesDatabase::class.java,
                    "likes_database"
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
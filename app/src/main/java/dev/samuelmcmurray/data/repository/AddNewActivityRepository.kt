package dev.samuelmcmurray.data.repository

class AddNewActivityRepository private constructor(private val database : String) {

    companion object {
        @Volatile
        private var instance : AddNewActivityRepository? = null

        /**
         * Creating a Singleton to pass information from database to
         * repository or to database from repository. So that we can move data to ViewModel
         * Repository handles most of the data
         */
        fun getInstance(database: String) =
            instance ?: synchronized(this) {
                instance ?: AddNewActivityRepository(database).also { instance = it }
            }
    }
}
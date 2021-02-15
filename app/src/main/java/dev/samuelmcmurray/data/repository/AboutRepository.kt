package dev.samuelmcmurray.data.repository

class AboutRepository private constructor(private val database : String) {
    //TODO: 1. Need to add the about database to constructor of type AboutDAO
    //TODO: 2. Need to add functions for adding to about and getting from about database
    // example fun addAbout(about: About) { aboutDAO.addAbout }


    companion object {
        @Volatile private var instance : AboutRepository? = null

        /**
         * Creating a Singleton to pass information from database to
         * repository or to database from repository. So that we can move data to ViewModel
         * Repository handles most of the data
         */
        fun getInstance(database: String) =
            instance ?: synchronized(this) {
                instance ?: AboutRepository(database).also { instance = it }
            }
    }
}
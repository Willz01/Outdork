package dev.samuelmcmurray.data.singelton

import android.net.Uri
import java.time.LocalDate

class NewPostSingleton private constructor(){

    companion object {
        val getInstance = NewPostSingleton()
    }

    var message: String? = null
    var date: LocalDate? = null
    var id : String? = null
    var image: Uri? = null
    var hasImage: Boolean = false
    var user: String? = null
    var userName: String? = null
    var comments: List<String>? = null
    var likes: Int? = null
    var downloadURL: String? = null
    var imageId: String? = null
}
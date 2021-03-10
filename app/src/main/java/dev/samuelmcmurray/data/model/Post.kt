package dev.samuelmcmurray.data.model

import android.net.Uri
import java.time.LocalDate

class Post {
    var message: String
    var date: LocalDate
    var id : String
    var image: Uri
    var hasImage: Boolean
    var user: String
    var userName: String
    var comments: List<String>
    var likes: Int
    var downloadURL: String
    var imageId: String

    constructor(
        message: String, date: LocalDate, id: String, image: Uri, hasImage: Boolean,
        user: String, likes: Int, userName: String, downloadURL: String, imageId: String) {
        this.message = message
        this.date = date
        this.id = id
        this.image = image
        this.hasImage = hasImage
        this.user = user
        this.comments = emptyList()
        this.likes = likes
        this.userName = userName
        this.downloadURL = downloadURL
        this.imageId = imageId
    }
}

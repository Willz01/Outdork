package dev.samuelmcmurray.data.model

import android.graphics.Bitmap
import android.net.Uri


class Post {
    var message: String
    var date: String
    var id : String
    var image: Uri
    var hasImage: Boolean
    var userId: String
    var userName: String
    var comments: List<String> = listOf()
    var comment = 0
    var likes: Int
    var downloadURL: String
    var userImageURL: String? = "nothing.com"
    var imageId: String
    var defaultProfileImage: Int = dev.samuelmcmurray.R.drawable.defaultprofile

    constructor(
        message: String, date: String, id: String, image: Uri, hasImage: Boolean,
        userId: String, likes: Int, userName: String, downloadURL: String, imageId: String) {
        this.message = message
        this.date = date
        this.id = id
        this.image = image
        this.hasImage = hasImage
        this.userId = userId
        this.likes = likes
        this.userName = userName
        this.downloadURL = downloadURL
        this.imageId = imageId
    }

}
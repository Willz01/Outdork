package dev.samuelmcmurray.data.model

import android.content.ContentResolver
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import dev.samuelmcmurray.R


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
    var downloadURL: String?
    var userImageURL: String? = ""
    var imageId: String
    var defaultProfileImage: Uri? = null

    constructor(
        message: String, date: String, id: String, image: Uri, hasImage: Boolean,
        userId: String, likes: Int, userName: String, downloadURL: String?, imageId: String) {
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
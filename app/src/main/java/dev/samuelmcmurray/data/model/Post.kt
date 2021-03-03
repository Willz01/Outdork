package dev.samuelmcmurray.data.model

import android.net.Uri

class Post {
    var message: String
    var id : Long
    var image: Uri
    var hasImage: Boolean
    var user: String
    var userName: String
    var comments: List<String>
    var likes: Int

    constructor(message: String, id: Long, image: Uri, hasImage: Boolean,
                user: String, likes: Int, userName: String) {
        this.message = message
        this.id = id
        this.image = image
        this.hasImage = hasImage
        this.user = user
        this.comments = emptyList()
        this.likes = likes
        this.userName = userName
    }
}

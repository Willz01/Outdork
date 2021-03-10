package dev.samuelmcmurray.data.model

import android.media.Image
import android.net.Uri


data class CurrentUser(
    val id: String,
    val firstName: String,
    val lastName: String,
    val userName: String,
    val email: String,
    val country: String,
    val state: String,
    val city: String,
    val dob: Long
) : User(id, firstName, lastName, userName, email, country, state, city, dob) {
    var about: String = ""
    var photo: Image? = null
    var activities: List<String>? = emptyList()
    var profileResource = "android.resource://${dev.samuelmcmurray.R.drawable.com_facebook_profile_picture_blank_portrait}"
    var profilePhoto = Uri.parse(profileResource)
    var age: Int = 0
}
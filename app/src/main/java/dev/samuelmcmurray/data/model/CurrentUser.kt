package dev.samuelmcmurray.data.model

import android.net.Uri
import com.google.type.Date

data class CurrentUser(
    val id: String,
    val firstName: String,
    val lastName: String,
    val userName: String,
    val email: String,
    val country: String,
    val state: String,
    val city: String,
    val dob: String
) : User(id, firstName, lastName, userName, email, country, state, city, dob) {

    var hasImage = false
    var about: String = ""
    var profileResource = "android.resource://${dev.samuelmcmurray.R.drawable.com_facebook_profile_picture_blank_portrait}"
    var activities: List<String>? = emptyList()
    var profilePhoto: String? = null
    var age: Int = 0
    var defaultProfileUri = Uri.parse("android.resource://${dev.samuelmcmurray.R.drawable.com_facebook_profile_picture_blank_portrait}")
}
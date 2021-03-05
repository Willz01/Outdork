package dev.samuelmcmurray.data.model

import android.media.Image


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
    var activities: List<String>? = null
    var photo: Image? = null
    var age: Int = 0
}
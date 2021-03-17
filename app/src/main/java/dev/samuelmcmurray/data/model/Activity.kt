package dev.samuelmcmurray.data.model

import com.google.android.gms.maps.model.LatLng

/**
 * Each activity is linked to a user (current user) and has a list of filters applied to it for search purpose
 */
data class Activity(val name : String, val currentUserID: String, var filter: List<String>, var latLng: LatLng, var rating : Double) {
}
package dev.samuelmcmurray.data.model

/**
 * Each activity is linked to a user (current user) and has a list of filters applied to it for search purpose
 */
data class Activity(val name : String, val currentUser: CurrentUser, var filter: List<String>) {
}
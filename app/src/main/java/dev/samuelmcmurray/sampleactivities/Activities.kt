package dev.samuelmcmurray.sampleactivities

import dev.samuelmcmurray.data.model.Activity
import dev.samuelmcmurray.data.model.CurrentUser

object Activities {

    fun returnActivities(): ArrayList<Activity> {
        val activities = ArrayList<Activity>()

        val user = CurrentUser(
            "0153",
            "Danny",
            "Frank",
            "superhiker0011",
            "mymail@gmail.com",
            "Sweden",
            "Kristianstad",
            "Nasby",
            "08-22-1995"
        )

        val trails =
            Activity("Trails", user, listOf("Dog walking", "Hiking", "Horse riding/trail", "Biking"))
        val water = Activity(
            "Water sports",
            user,
            listOf("Swimming", "Fishing")
        )
        val passive =
            Activity("Spotting activities", user, listOf("Bird watching", "Scenic views"))

        activities.add(trails)
        activities.add(water)
        activities.add(passive)

        return activities
    }
}
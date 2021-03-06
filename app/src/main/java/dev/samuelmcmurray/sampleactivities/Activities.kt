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

        val hike =
            Activity("Hike", user, listOf("Long path", "Short trees", "Tall trees", "Short path"))
        val walk = Activity(
            "Walk",
            user,
            listOf("Road side", "Busy complex", "Water side", "Quiet resident")
        )
        val run =
            Activity("Run", user, listOf("Long run", "Short run", "Bush path", "Residential area"))

        activities.add(hike)
        activities.add(walk)
        activities.add(run)

        return activities
    }
}
package dev.samuelmcmurray.sampleactivities

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import dev.samuelmcmurray.data.model.Activity
import dev.samuelmcmurray.data.model.ActivityHolder
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton

private const val TAG = "Activities"


val user = CurrentUserSingleton.getInstance.currentUser?.id

object Activities {

    init {
       // setDefaultActivities().map { activity -> addActivity(activity) }
    }

   /* private fun setDefaultActivities(): ArrayList<Activity> {

        val activities = ArrayList<Activity>()
        // water activities
        val norra = Activity(
            "Norra Lingenäset",
            user!!,
            listOf("Dog walking", "Hiking", "Horse riding/trail", "Biking"),
            LatLng(56.072899979172696, 14.131917622395449),4.0
        )
        val beach = Activity(
            "Täppet Havsbad",
            user,
            listOf("Swimming", "Fishing", "Bird watching", "Scenic views"),
            LatLng(55.93656239368784, 14.320014906827728),4.5
        )
        val club = Activity(
            "C4SS Jolleklubb",
            user,
            listOf("Swimming", "Fishing"),
            LatLng(55.92682971794225, 14.317545135892214),3.5
        )
        val waterSide = Activity(
            "Brostorp Fiske",
            user,
            listOf("Fishing"),
            LatLng(55.966564166718456, 13.80409499803521),4.1
        )
        val waterSide2 = Activity(
            "Bottenstugan i Svartedalen",
            user,
            listOf("Swimming", "Fishing", "Bird watching", "Scenic views", "Trails"),
            LatLng(58.00545610133644, 11.990938791845457),4.0
        )

        // trails
        val bockeboda = Activity(
            "Bockeboda",
            user,
            listOf("Dog walking", "Hiking", "Horse riding/trail", "Biking"),
            LatLng(56.03774447854945, 13.994504346595201),5.0
        )
        val centrum = Activity(
            "Linnérundan, besöksplats i Vattenriket",
            user,
            listOf("Dog walking", "Hiking", "Horse riding/trail", "Biking", "Swimming"),
            LatLng(56.03150155960876, 14.14929651124375),3.4
        )
        val vedema = Activity(
            "Utsikten i Vedema",
            user,
            listOf(
                "Dog walking",
                "Hiking",
                "Horse riding/trail",
                "Biking",
                "Bird watching",
                "Scenic views", "Fishing"
            ),
            LatLng(56.21337354376602, 13.639072189521945),5.0
        )
        val stackedala = Activity(
            "Stackedala",
            user,
            listOf("Dog walking", "Hiking", "Bird watching", "Scenic views"),
            LatLng(55.919643951002435, 13.97314006260376),4.2
        )

        // Spotting activities
        val birdsThing = Activity(
            "Pulken, besöksplats i Vattenriket",
            user,
            listOf("Bird watching", "Scenic views"),
            LatLng(55.89024004136211, 14.20804766814821),4.1
        )
        val camp = Activity(
            "Landöns Camping",
            user,
            listOf("Bird watching", "Scenic views"),
            LatLng(55.974796661817976, 14.408706865317669),3.1
        )

        val haltaTrail = Activity(
            "Hålta", user, listOf("Dog walking", "Hiking"),
            LatLng(57.8950202405848, 11.828057444026108),4.0
        )

        val blueTrail = Activity(
            "Blue Trail", user, listOf("Dog walking", "Hiking"),
            LatLng(57.89900223207865, 11.609726621946866),4.0
        )

        val tofta =
            Activity(
                "Tofta naturreservat", user, listOf("Dog walking", "Hiking", "Swmming"),
                LatLng(57.85581642303297, 11.696950114072678),4.6
            )

        activities.add(norra)
        activities.add(beach)
        activities.add(club)
        activities.add(waterSide)
        activities.add(waterSide2)

        activities.add(bockeboda)
        activities.add(centrum)
        activities.add(vedema)
        activities.add(stackedala)

        activities.add(birdsThing)
        activities.add(camp)
        activities.add(haltaTrail)
        activities.add(blueTrail)
        activities.add(tofta)

        return activities
    }*/

    fun returnActivitiesHolder(): ArrayList<ActivityHolder> {
        val activitiesHolder = ArrayList<ActivityHolder>()


        val trails =
            ActivityHolder(
                "Trails",
                user!!,
                listOf("Dog walking", "Hiking", "Horse riding/trail", "Biking")
            )
        val water = ActivityHolder(
            "Water sports",
            user,
            listOf("Swimming", "Fishing")
        )
        val passive =
            ActivityHolder("Spotting activities", user, listOf("Bird watching", "Scenic views"))

        activitiesHolder.add(trails)
        activitiesHolder.add(water)
        activitiesHolder.add(passive)


        return activitiesHolder
    }


    fun addActivity(activity: Activity) {
        val activityRef = FirebaseFirestore.getInstance().collection("activities")
        val activityMap = hashMapOf(
            "name" to activity.name,
            "UID" to activity.currentUserID,
            "filters" to activity.filter,
            "latlng" to activity.latLng,
            "rating" to activity.rating
        )

        activityRef.add(activityMap).addOnSuccessListener {
            Log.d(TAG, "addActivity: Activity added")
        }.addOnFailureListener {

        }

    }
}
package dev.samuelmcmurray.data.repository

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import dev.samuelmcmurray.data.model.Activity
import dev.samuelmcmurray.ui.find_new_activity.location.MyCallback

private const val TAG = "SelectRouteRepository"

@Suppress("UNCHECKED_CAST")
class SelectRouteRepository {

    fun getActivities(myCallback: MyCallback){

        val activitiesRef = FirebaseFirestore.getInstance().collection("activities")

        activitiesRef.get().addOnSuccessListener { activities ->
            if(!activities.isEmpty){
                val activitiesList = ArrayList<Activity>()
                for (activity in activities) {

                    Log.d(TAG, "getActivities: ${activity.id} => ${activity.data}")

                    val name = activity.data["name"]
                    val UID = activity.data["UID"]
                    val filters = activity.data["filters"]
                    val latLng = activity.data["latlng"]


                    val array = latLng.toString().split(", ")
                    val lat = array[0].toString().substring(10, array[0].toString().length)
                    val long = array[1].toString().substring(10, array[1].toString().length - 1)
                    val latLngObject = LatLng(lat.toDouble(), long.toDouble())
                    Log.d(TAG, "getActivities: $lat - $long")

                    activitiesList.add(
                        Activity(
                            name.toString(), UID.toString(),
                            filters as List<String>, latLngObject
                        )
                    )
                }

                myCallback.onCallback(activitiesList)
            }

          //  Log.d(TAG, "getActivities -x--> : ${activitiesList.size}")
        }.addOnFailureListener { e ->
            Log.d(
                TAG,
                "getActivities: Error reading activities [caused by : $e]"
            )
        }


    }
}
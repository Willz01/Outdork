package dev.samuelmcmurray.data.repository

import android.app.Activity
import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddNewActivityRepository{
    private lateinit var application: Application
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    private var fireStore : FirebaseFirestore? = null





  /*  fun addActivity(activity: Activity){
        val activityRef = FirebaseFirestore.getInstance().collection("activity")
        activityRef.do
    }*/
}
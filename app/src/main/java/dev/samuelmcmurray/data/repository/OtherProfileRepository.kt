package dev.samuelmcmurray.data.repository

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.Post


private const val TAG = "OtherProfileRepository"
class OtherProfileRepository {

    private var application: Application
    var postsListLiveData: MutableLiveData<List<Post>>


    constructor(application: Application) {
        this.application = application
        postsListLiveData = MutableLiveData()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getPostsList() {
        var posts: MutableList<Post> = mutableListOf()
        val postRef = FirebaseFirestore.getInstance().collection("Posts")
        postRef.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
            var i = 0
            for (document in result) {
                val comments = document["comments"].toString()
                val date = document["date"].toString()
                val downloadURL = document["downloadURL"].toString()
                val hasImage = document["hasImage"].toString().toBoolean()
                val commentList: List<String> = document["comment"].toString().split(",")
                val message = document["message"].toString()
                val userName = document["userName"].toString()
                val userId = document["userID"].toString()
                val likes = document["likes"].toString()
                val userProfileImageURL = document["userImageURL"].toString()
                val imageId = document["imageId"].toString()
                val id = document["id"].toString()
                val post = Post(message, date, id, downloadURL.toUri(), hasImage, userId, likes.toInt(),
                    userName, downloadURL, imageId)
                post.comments = commentList
                post.comment = comments.toInt()
                post.userImageURL = userProfileImageURL
                posts.add(i, post)
                i++
            }
            val post1 = Post(
                "Great hike today at the high hill sides, with my great hiking partner @superhiker2324",
                "12/23/89",
                "1987kdasdf823",
                Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        application.resources.getResourcePackageName(R.drawable.hike_image1) + '/' +
                        application.resources.getResourceTypeName(R.drawable.hike_image1) + '/' +
                        R.drawable.hike_image1.toString()),
                false,
                "jdsioje309u3ijkew",
                5,
                "Mr Darcy",
                null,
                "jkdsyu89jkdu9")
            post1.defaultProfileImage = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    application.resources.getResourcePackageName(R.drawable.hiker_pp1) + '/' +
                    application.resources.getResourceTypeName(R.drawable.hiker_pp1) + '/' +
                    R.drawable.hiker_pp1.toString())
            posts.add(i++, post1)

            val post2 = Post(
                "hello another post",
                "19/55/62",
                "djkshe983kljdf",
                Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        application.resources.getResourcePackageName(R.drawable.hike_image2) + '/' +
                        application.resources.getResourceTypeName(R.drawable.hike_image2) + '/' +
                        R.drawable.hike_image2.toString()),
                false,
                "sd89ajio89kf89sd",
                5,
                "superhiker2324",
                null,
                "jdsiojodifwerjijklsd"
            )
            post2.defaultProfileImage = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    application.resources.getResourcePackageName(R.drawable.hiker_pp2) + '/' +
                    application.resources.getResourceTypeName(R.drawable.hiker_pp2) + '/' +
                    R.drawable.hiker_pp2.toString())
            posts.add(i++, post2)
            val post3 = Post(
                "another poist",
                "14/56/95",
                "djkshedsfjdf",
                Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        application.resources.getResourcePackageName(R.drawable.hike_image3) + '/' +
                        application.resources.getResourceTypeName(R.drawable.hike_image3) + '/' +
                        R.drawable.hike_image3.toString()),
                false,
                "sd89ajio89kfsdfd89sd",
                1,
                "mY dOg",
                null,
                "jdsiojodifwedklsd"
            )
            post3.defaultProfileImage = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    application.resources.getResourcePackageName(R.drawable.hiker_pp3) + '/' +
                    application.resources.getResourceTypeName(R.drawable.hiker_pp3) + '/' +
                    R.drawable.hiker_pp3.toString())
            posts.add(i++, post3)
            val post4 = Post(
                "the last post",
                "21/15/13",
                "djkshedsfjdf",
                Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        application.resources.getResourcePackageName(R.drawable.hike_image4) + '/' +
                        application.resources.getResourceTypeName(R.drawable.hike_image4) + '/' +
                        R.drawable.hike_image4.toString()),
                false,
                "sd899kfsdfd89sd",
                1,
                "Superman",
                null,
                "jdsdifwedklsd"
            )
            post4.defaultProfileImage = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    application.resources.getResourcePackageName(R.drawable.hiker_pp4) + '/' +
                    application.resources.getResourceTypeName(R.drawable.hiker_pp4) + '/' +
                    R.drawable.hiker_pp4.toString())
            posts.add(i, post4)
            postsListLiveData.postValue(posts)

            Toast.makeText(
                application.applicationContext,
                "Discoveries posted.",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener { exception ->
            Log.d(TAG, "getPostsList: $exception")
            Toast.makeText(
                application.applicationContext,
                "Discoveries failed to download",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun updateLikes(uid: String, postID: String, likes: Int) {
        val db = FirebaseFirestore.getInstance().collection("Posts")
            .document("$uid$postID")
        db.update("likes", likes)
            .addOnSuccessListener {
                Log.d(TAG, "updateLikes: Success")
            }.addOnFailureListener {
                Log.d(TAG, "updateLikes: Failed $it")
            }
    }

}
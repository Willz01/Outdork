package dev.samuelmcmurray.data.repository

import android.app.Application
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.model.Post
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import java.io.File
import kotlin.coroutines.coroutineContext


private const val TAG = "DiscoveriesRepository"
class DiscoveriesRepository {
    private var application: Application
    var storage = FirebaseStorage.getInstance("gs://UserPhotos")
    var firebaseAuth = FirebaseAuth.getInstance()
    var postCreatedLiveData: MutableLiveData<Boolean>
    var storeImageLiveData: MutableLiveData<Boolean>
    var userLiveData: MutableLiveData<CurrentUser>


    constructor(application: Application) {
        this.application = application
        postCreatedLiveData = MutableLiveData()
        storeImageLiveData = MutableLiveData()
        userLiveData = MutableLiveData()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun newPost(message: String, image: Uri, hasImage: Boolean, likes: Int) {
        val id: Long = Calendar.getInstance().time.time
        val uid = CurrentUserSingleton.getInstance.currentUser!!.id
        val userName = CurrentUserSingleton.getInstance.currentUser!!.userName
        val db: DocumentReference = FirebaseFirestore.getInstance().document("Posts/${uid}")
        val userPost = Post(
            message,
            id,
            image,
            hasImage,
            uid,
            likes,
            userName
        )
        if (image != Uri.EMPTY) {
            storeImage(uid, id, image)
        }

        val post = hashMapOf(
            "message" to message,
            "id" to id,
            "userName" to userName,
            "hasImage" to hasImage,
            "comments" to userPost.comments,
            "likes" to likes
        )
        db.set(post)
            .addOnSuccessListener(ContextCompat.getMainExecutor(application), { void ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: $uid"
                )
                postCreatedLiveData.postValue(true)
            })
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                postCreatedLiveData.postValue(false)
            }

    }

    fun storeImage(uid: String, id: Long, image: Uri) {
        val storageReference = storage.reference
        storageReference.child("$uid/$id.jpg")
        var uploadTask = storageReference.putFile(image)
        uploadTask.addOnFailureListener {
            storeImageLiveData.postValue(false)
        }.addOnSuccessListener {
            storeImageLiveData.postValue(true)
        }
    }

    fun getCurrentUser() {
        val userRef = FirebaseFirestore.getInstance().collection("Users")
        var userID = firebaseAuth.currentUser?.uid
        userRef.document(userID.toString()).get()
            .addOnSuccessListener {
                    result ->
                val firstName = result["firstName"]
                val lastName = result["lastName"]
                val userName = result["userName"]
                val email = result["email"]
                val dateOfBirth = result["dateOfBirth"]
                val about = result["about"]
                val hasImage = result["hasImage"]
                val city = result["city"]
                val country = result["country"]
                val state = result["state"]
                val currentUser = CurrentUser(userID.toString(), firstName.toString(), lastName.toString(),
                    userName.toString(), email.toString(), country.toString(), state.toString(),
                    city.toString(), dateOfBirth.toString().toLong())
                currentUser.about = about.toString()
                CurrentUserSingleton.getInstance.currentUser = currentUser
                CurrentUserSingleton.getInstance.currentUser!!.hasImage = hasImage.toString().toBoolean()
                userLiveData.postValue(currentUser)
                Log.d(TAG, "getCurrentUser: DocumentSnapshot retrieved with ID: $userID")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }
}
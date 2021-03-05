package dev.samuelmcmurray.data.repository

import android.app.Application
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.model.Post
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import java.time.LocalDate
import java.time.Period


private const val TAG = "DiscoveriesRepository"
class DiscoveriesRepository {
    private var application: Application
    val firebaseApplication = FirebaseApp.getInstance()
    var storage = FirebaseStorage.getInstance(firebaseApplication, "gs://outdork-788e0.appspot.com")
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


    @RequiresApi(Build.VERSION_CODES.O)
    fun newPost(message: String, hasImage: Boolean, likes: Int) {
        Log.d(TAG, "newPost: top ${CurrentUserSingleton.getInstance.currentUser}")
        val id = LocalDate.now()
        val uid = CurrentUserSingleton.getInstance.currentUser!!.id
        val userName = CurrentUserSingleton.getInstance.currentUser!!.userName
        val db: DocumentReference = FirebaseFirestore.getInstance().document("Posts/${uid}")
        val userPost = Post(
            message,
            id,
            Uri.EMPTY,
            hasImage,
            uid,
            likes,
            userName
        )
        Log.d(TAG, "newPost: ${userPost.toString()}")
//        if (image != Uri.EMPTY || image != null ) {
//            uploadImageToFirebase(uid, id.toString(), image)
//        }

        val post = hashMapOf(
            "message" to message,
            "id" to id.toString(),
            "userID" to uid,
            "userName" to userName,
            "hasImage" to hasImage,
            "likes" to likes
        )
        db.set(post)
            .addOnSuccessListener { void ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: $uid"
                )
                postCreatedLiveData.postValue(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                postCreatedLiveData.postValue(false)
            }

    }

    private fun uploadImageToFirebase(uid: String, id: String, contentUri: Uri) {
        val image: StorageReference = storage.reference.child("UserPhotos/$uid/$id")
        image.putFile(contentUri).addOnSuccessListener {
            image.downloadUrl.addOnSuccessListener { uri ->
                Log.d("tag", "onSuccess: Uploaded Image URl is $uri")
            }
            Toast.makeText(application.applicationContext, "Image Is Uploaded.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(
                application.applicationContext,
                "Upload Failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentUser() {
        val userRef = FirebaseFirestore.getInstance().collection("Users")
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        userRef.document(userID.toString()).get()
            .addOnSuccessListener { result ->
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
                Log.d(TAG, "getCurrentUser: ${dateOfBirth.toString()}")
                val currentUser = CurrentUser(
                    userID.toString(), firstName.toString(), lastName.toString(),
                    userName.toString(), email.toString(), country.toString(), state.toString(),
                    city.toString(), dateOfBirth.toString()
                )

                currentUser.age = getAge(dateOfBirth.toString())
                Log.d(TAG, "getCurrentUser: ${currentUser.age}")
                currentUser.about = about.toString()
                currentUser.hasImage = hasImage.toString().toBoolean()

                CurrentUserSingleton.getInstance.currentUser = currentUser
                userLiveData.postValue(currentUser)
                Log.d(TAG, "getCurrentUser: DocumentSnapshot retrieved with ID: $userID")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAge(dob: String) : Int {
        val parts = dob.split("-")
        val year = parts[2]
        val day = parts[1]
        val month = parts[0]
        return Period.between(
            LocalDate.of(year.toInt(), month.toInt(), day.toInt()),
            LocalDate.now()
        ).years
    }
}
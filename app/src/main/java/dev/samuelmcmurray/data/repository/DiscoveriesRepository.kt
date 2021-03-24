package dev.samuelmcmurray.data.repository

import android.app.Application
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.model.Post
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.data.singelton.NewPostSingleton
import java.time.LocalDate
import java.time.Period
import java.util.*


private const val TAG = "DiscoveriesRepository"
class DiscoveriesRepository {
    private var application: Application
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
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
    fun newPost(message: String, image: Uri, hasImage: Boolean, likes: Int) {
        if (image != Uri.EMPTY) {
            uploadImageToFirebase(image)
        }
        Log.d(TAG, "newPost: top ${CurrentUserSingleton.getInstance.currentUser}")
        var downloadURL = NewPostSingleton.getInstance.downloadURL
        var imageId = NewPostSingleton.getInstance.imageId
        while (downloadURL == null || imageId == null) {
            downloadURL = NewPostSingleton.getInstance.downloadURL
            imageId = NewPostSingleton.getInstance.imageId
        }
        val id = System.currentTimeMillis().toString()
        val date = LocalDate.now()
        val uid = CurrentUserSingleton.getInstance.currentUser!!.id
        val userName = CurrentUserSingleton.getInstance.currentUser!!.userName
        val db: DocumentReference = FirebaseFirestore.getInstance().document("Posts/${uid}${id}")
        val userPost = Post(
            message,
            date,
            id,
            image,
            hasImage,
            uid,
            likes,
            userName,
            downloadURL!!,
            imageId
        )
        Log.d(TAG, "newPost: ${userPost.toString()}")


        val post = hashMapOf(
            "message" to message,
            "date" to date.toString(),
            "id" to id,
            "userID" to uid,
            "userName" to userName,
            "imageId" to imageId,
            "hasImage" to hasImage,
            "downloadURL" to downloadURL,
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

    private fun uploadImageToFirebase(contentUri: Uri) {
        storage = FirebaseStorage.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
        NewPostSingleton.getInstance.imageId = UUID.randomUUID().toString()
        val imageId = NewPostSingleton.getInstance.imageId
        val image = storageRef!!.child("UserPhotos/$imageId" )
        val uploadTask = image.putFile(contentUri)
        val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                Log.d("tag", "Failure: Uploaded Image URi is $contentUri")
                Toast.makeText(
                    application.applicationContext,
                    "Upload Failed.",
                    Toast.LENGTH_SHORT
                ).show()
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation image.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    application.applicationContext,
                    "Image Is Uploaded.",
                    Toast.LENGTH_SHORT
                ).show()
                val downloadUri = task.result
                NewPostSingleton.getInstance.downloadURL = downloadUri.toString()
            }
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
//        val parts = dob.split("-")
//        val year = parts[2]
//        val day = parts[1]
//        val month = parts[0]
        return 21
//        Period.between(
//            LocalDate.of(year.toInt(), month.toInt(), day.toInt()),
//            LocalDate.now()
//        ).years
    }
}
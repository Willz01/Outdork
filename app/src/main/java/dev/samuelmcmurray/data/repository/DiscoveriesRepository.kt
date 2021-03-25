package dev.samuelmcmurray.data.repository

import android.app.Application
import android.content.ContentResolver
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.CurrentUser
import dev.samuelmcmurray.data.model.Post
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.data.singelton.NewPostSingleton
import kotlinx.coroutines.delay
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
    var postsListLiveData: MutableLiveData<List<Post>>
    var downloadURLLiveData: MutableLiveData<Boolean>


    constructor(application: Application) {
        this.application = application
        postCreatedLiveData = MutableLiveData()
        storeImageLiveData = MutableLiveData()
        userLiveData = MutableLiveData()
        postsListLiveData = MutableLiveData()
        downloadURLLiveData = MutableLiveData()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun newPost(message: String, image: Uri, hasImage: Boolean, likes: Int) {
        delay(1000)
        var userProfileImageURL : String? = ""
        Log.d(TAG, "newPost: top ${CurrentUserSingleton.getInstance.currentUser}")
        var downloadURL = NewPostSingleton.getInstance.downloadURL
        var imageId = NewPostSingleton.getInstance.imageId
        while (downloadURL.isNullOrEmpty() || imageId.isNullOrEmpty()) {
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
            date.toString(),
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
        if (CurrentUserSingleton.getInstance.currentUser!!.hasImage) {
            userProfileImageURL = CurrentUserSingleton.getInstance.currentUser!!.profilePhoto
        }

        val post = hashMapOf(
            "message" to message,
            "date" to date.toString(),
            "id" to id,
            "userID" to uid,
            "imageUri" to image.toString(),
            "userImageURL" to userProfileImageURL.toString(),
            "comments" to 0,
            "userName" to userName,
            "imageId" to imageId,
            "hasImage" to hasImage,
            "downloadURL" to downloadURL.toString(),
            "timestamp" to FieldValue.serverTimestamp(),
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

    fun uploadImageToFirebase(contentUri: Uri) {
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
                downloadURLLiveData.postValue(true)
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentUser() {
        val userRef = FirebaseFirestore.getInstance().collection("Users")
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        userRef.document(userID.toString()).get()
            .addOnSuccessListener { result ->
                val firstName = result["firstName"].toString()
                val lastName = result["lastName"].toString()
                val userName = result["userName"].toString()
                val email = result["email"].toString()
                val dateOfBirth = result["dateOfBirth"].toString()
                val about = result["about"].toString()
                val hasImage = result["hasImage"].toString().toBoolean()
                val userProfileImageURL = result["userImageURL"].toString()
                val city = result["city"].toString()
                val country = result["country"].toString()
                val state = result["state"].toString()
                Log.d(TAG, "getCurrentUser: ${dateOfBirth.toString()}")
                val currentUser = CurrentUser(
                    userID.toString(), firstName, lastName,
                    userName, email, country, state,
                    city, dateOfBirth
                )
                if (hasImage) {
                    currentUser.profilePhoto = userProfileImageURL
                }

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    currentUser.age = getAge(dateOfBirth)
                } else {
                    getRelativeAge(dateOfBirth)
                }
                Log.d(TAG, "getCurrentUser: ${currentUser.age}")
                currentUser.about = about
                currentUser.hasImage = hasImage

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

    private fun getRelativeAge(dob: String) {
        //val parts = dob.split("-")
        //val year = parts[2]
        //val day = parts[1]
        //val month = parts[0]
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
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        application.resources.getResourcePackageName(R.drawable.hike_image1) + '/' +
                        application.resources.getResourceTypeName(R.drawable.hike_image1) + '/' +
                        R.drawable.hike_image1.toString()),
                false,
                "jdsioje309u3ijkew",
                5,
                "Mr Darcy",
                null,
                "jkdsyu89jkdu9")
            post1.defaultProfileImage = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    application.resources.getResourcePackageName(R.drawable.hiker_pp1) + '/' +
                    application.resources.getResourceTypeName(R.drawable.hiker_pp1) + '/' +
                    R.drawable.hiker_pp1.toString())
            posts.add(i++, post1)

            val post2 = Post(
                "hello another post",
                "19/55/62",
                "djkshe983kljdf",
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
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
            post2.defaultProfileImage = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    application.resources.getResourcePackageName(R.drawable.hiker_pp2) + '/' +
                    application.resources.getResourceTypeName(R.drawable.hiker_pp2) + '/' +
                    R.drawable.hiker_pp2.toString())
            posts.add(i++, post2)
            val post3 = Post(
                "another poist",
                "14/56/95",
                "djkshedsfjdf",
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
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
            post3.defaultProfileImage = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    application.resources.getResourcePackageName(R.drawable.hiker_pp3) + '/' +
                    application.resources.getResourceTypeName(R.drawable.hiker_pp3) + '/' +
                    R.drawable.hiker_pp3.toString())
            posts.add(i++, post3)
            val post4 = Post(
                "the last post",
                "21/15/13",
                "djkshedsfjdf",
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
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
            post4.defaultProfileImage = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
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


package dev.samuelmcmurray.ui.post

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareButton
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.OtherUser
import dev.samuelmcmurray.data.model.Post
import dev.samuelmcmurray.data.singelton.OtherUserSingleton
import dev.samuelmcmurray.ui.discoveries.DiscoveriesFragment
import dev.samuelmcmurray.ui.discoveries.DiscoveriesViewModel
import dev.samuelmcmurray.ui.favorite.FavoriteViewModel
import dev.samuelmcmurray.ui.like.Like
import dev.samuelmcmurray.ui.like.LikeViewModel
import dev.samuelmcmurray.ui.profile.OtherProfileFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "PostAdapter"

class PostAdapter(private val list: List<Post>, var context: Context):
    RecyclerView.Adapter<PostViewHolder>() {

    private val likeList = getLikeList()

    var data = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return PostViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post: Post = list[position]
        val modelPost = list[position]

        //TODO fix this so it actually makes sense these fields need to be changed
        //val favoritePost = Post
        CoroutineScope(Dispatchers.Main).launch {
            holder.bind(post, context, context.applicationContext as Application)
        }



        val profileImage = holder.itemView.findViewById<ImageView>(R.id.profile_image)
        profileImage.setOnClickListener {
            val intent = Intent(context, OtherProfileFragment::class.java)
            val otherUser = OtherUser(post.userId, "", "", post.userName, "",
                "", "", "", "")
            OtherUserSingleton.getInstance.otherUser = otherUser
        }

        val likeButton = holder.itemView.findViewById<CheckBox>(R.id.like_button)
        val likes = holder.itemView.findViewById<TextView>(R.id.textViewLikeCount)

        if (checkLikeList(modelPost.userId, modelPost.id)) {
            likeButton.isChecked = true
            likeButton.setBackgroundResource(R.drawable.ic_baseline_thumb_up_blue_24)
        }


        likeButton.setOnClickListener {

            if (likeButton.isChecked) {
                likeButton.setBackgroundResource(R.drawable.ic_baseline_thumb_up_blue_24)
                modelPost.likes = modelPost.likes + 1
                likes.text = modelPost.likes.toString()
                likeButton.isChecked = true
                updateLikePost(modelPost.userId, modelPost.id, modelPost.likes)
                addLikeLocal(modelPost.userId, modelPost.id)
            } else {
                likeButton.setBackgroundResource(R.drawable.ic_baseline_thumb_up_black_24)
                modelPost.likes = modelPost.likes -1
                likes.text = modelPost.likes.toString()
                likeButton.isChecked = false
                updateLikePost(modelPost.userId, modelPost.id, modelPost.likes)
                removeLikeLocal(modelPost.userId, modelPost.id)
            }
        }

        holder.itemView.findViewById<TextView>(R.id.option_menu_txt).setOnClickListener {
            val popupMenu = PopupMenu(context, holder.itemView.findViewById(R.id.option_menu_txt))
            popupMenu.inflate(R.menu.post_option_menu)

//            popupMenu.setOnMenuItemClickListener { item ->
//
//                addToBookmarks(favoritePost, item, context, holder)
//            }
//            popupMenu.show()
        }
    }

    override fun getItemCount(): Int = list.size

    private fun viewOtherProfile(post: Post) {
        val viewModel = DiscoveriesViewModel(context.applicationContext as Application)
        val otherUser = OtherUser(post.userId, "", "", post.userName, "",
        "", "", "", "")
        OtherUserSingleton.getInstance.otherUser = otherUser

    }

    private fun updateLikePost (postUser: String, postId: String, likes: Int) {
        val viewModel = DiscoveriesViewModel(context.applicationContext as Application)
        viewModel.updateLikes(postUser, postId, likes)
    }

    private fun addLikeLocal(uid: String, postID: String){
        val viewModel = LikeViewModel(context.applicationContext as Application)
        val like = Like(0, "$uid$postID")
        viewModel.addLike(like)

    }

    private fun removeLikeLocal(uid: String, postID: String) {
        val viewModel = LikeViewModel(context.applicationContext as Application)
        val like = Like(0, "$uid$postID")
        viewModel.removeLike(like)
    }

    private fun getLikeList() : List<Like> {
        val viewModel = LikeViewModel(context.applicationContext as Application)
        val likes = viewModel.readAllLike
        if (likes.isEmpty()) {
            return listOf()
        }
        return likes
    }

    private fun checkLikeList(uid: String, postID: String): Boolean {

        for(item in likeList) {
            if (item.postID == "$uid$postID") {
                return true
            }
        }
        return false
    }

//more generic name
    private fun addToBookmarks(
    post: PostLocal,
    item: MenuItem,
    context: Context,
    holder: PostViewHolder
    ): Boolean {
        val bookmarksViewModel =
            FavoriteViewModel(context.applicationContext as Application)

        if (item != null) {
            when (item.itemId) {
                R.id.favourite -> {
                    val list = bookmarksViewModel.readAllPost
                    var alreadyInFavorites = false
                    for (postItem in list) {
                        if (postItem.postID == post.postID) {
                            alreadyInFavorites = true
                            return true
                        }
                    }
                    if (alreadyInFavorites) {
                        Toast.makeText(
                            context,
                            "Post already in favourites",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        bookmarksViewModel.addPost(post)
                        Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Log.d(TAG, "onBindViewHolder: ${list.size} ")

                }
                R.id.share -> {
                    Toast.makeText(
                        context,
                        "Time to share to facebook",
                        Toast.LENGTH_SHORT
                    ).show()
                    val shareButton =
                        holder.itemView.findViewById<ShareButton>(R.id.share_button)

                    /**
                     * Share app link [Have to be published on the store]
                     * Using the facebook link as test.
                     * https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                     */

                    val shareLinkContent =
                        ShareLinkContent.Builder()
                            .setQuote(post.content.trim())
                            .setContentUrl(Uri.parse("https://www.youtube.com/"))
                            .setShareHashtag(
                                ShareHashtag.Builder().setHashtag("#OUTDORK").build()
                            ).build()


                    shareButton.shareContent = shareLinkContent
                    shareButton.performClick()
                }

                R.id.report -> Toast.makeText(context, "Reported", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return false

    }



}
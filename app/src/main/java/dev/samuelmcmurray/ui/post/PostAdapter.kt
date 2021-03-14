package dev.samuelmcmurray.ui.post

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareButton
import dev.samuelmcmurray.R
import dev.samuelmcmurray.ui.favorite.FavoriteViewModel

private const val TAG = "PostAdapter"

class PostAdapter(private val list: List<dev.samuelmcmurray.data.model.Post>, var context: Context):
    RecyclerView.Adapter<PostViewHolder>() {


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
        val post: dev.samuelmcmurray.data.model.Post = list[position]
        val modelPost = list[position]
        //TODO fix this so it actually makes sense these fields need to be changed
        //val favoritePost = Post(modelPost.id.toInt(), )
        holder.bindWithoutImage(post, context)



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


    private fun addToBookmarks(
        post: Post,
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
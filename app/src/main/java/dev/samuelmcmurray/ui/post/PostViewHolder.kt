package dev.samuelmcmurray.ui.post


import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import dev.samuelmcmurray.data.model.Post
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.samuelmcmurray.R
import dev.samuelmcmurray.utilities.GlideApp


private const val TAG = "PostViewHolder"
class PostViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.post_item, parent, false)) {
    private var mPosterView: TextView? = null
    private var mDateView: TextView? = null
    private var mContentView: TextView? = null
    private var optionMenu: TextView? = null
    private var like: CheckBox? = null
    private var likeCount: TextView? = null
    private var comment: CheckBox? = null
    private var commentCount: TextView? = null
    private var profilePicture: ImageView? = null
    private var imagePost: ImageView? = null
    private var fragment: Fragment? = null



    init {
        mPosterView = itemView.findViewById(R.id.postee_text)
        mDateView = itemView.findViewById(R.id.date_text)
        mContentView = itemView.findViewById(R.id.content_text)
        optionMenu = itemView.findViewById(R.id.option_menu_txt)
        like = itemView.findViewById(R.id.like_button)
        likeCount = itemView.findViewById(R.id.textViewLikeCount)
        commentCount = itemView.findViewById(R.id.textViewCommentCount)
        fragment = itemView.findViewById(R.id.discoveries_fragment)

        profilePicture = itemView.findViewById(R.id.profile_image)
        imagePost = itemView.findViewById(R.id.image_post)
    }

    fun bind(post: Post, context: Context, application: Application) {
        mPosterView?.text = post.userName
        mDateView?.text = post.date
        mContentView?.text = post.message
        likeCount?.text = post.likes.toString()
        commentCount?.text = post.comment.toString()
        if (!post.userImageURL.equals("") && post.userImageURL != null) {
            GlideApp.with(context).load(post.userImageURL).into(profilePicture!!)
        } else if (post.defaultProfileImage == null){
            post.defaultProfileImage = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        application.resources.getResourcePackageName(R.drawable.defaultprofile) + '/' +
                        application.resources.getResourceTypeName(R.drawable.defaultprofile) + '/' +
                        R.drawable.defaultprofile.toString())
            GlideApp.with(context).load(post.defaultProfileImage).into(profilePicture!!)
        } else {
            GlideApp.with(context).load(post.defaultProfileImage).into(profilePicture!!)
        }
        if (post.hasImage) {
            Glide.with(context).load(post.downloadURL).centerCrop().override(400,240).into(imagePost!!)
        } else if (post.downloadURL.isNullOrEmpty()) {
            Glide.with(context).load(post.image).centerCrop().override(400,240).into(imagePost!!)
        } else {
            imagePost?.visibility = View.GONE
        }
    }


}
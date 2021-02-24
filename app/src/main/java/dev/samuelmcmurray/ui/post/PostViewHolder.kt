package dev.samuelmcmurray.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import org.w3c.dom.Text

class PostViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.post_item, parent, false)) {
    private var mPosterView: TextView? = null
    private var mDateView: TextView? = null
    private var mContentView: TextView? = null
    private var optionMenu : TextView? = null

    private var rating : RatingBar? = null
    private var profilePicture : ImageView? = null
    private var imagePost : ImageView? = null



    init {
        mPosterView = itemView.findViewById(R.id.postee_text)
        mDateView = itemView.findViewById(R.id.date_text)
        mContentView = itemView.findViewById(R.id.content_text)
        optionMenu = itemView.findViewById(R.id.option_menu_txt)
        rating = itemView.findViewById(R.id.rating)
        profilePicture = itemView.findViewById(R.id.profile_image)
        imagePost = itemView.findViewById(R.id.image_post)
    }

    fun bind(post: Post) {
        mPosterView?.text = post.poster
        mDateView?.text = post.date
        mContentView?.text = post.content
        rating?.rating = post.rating.toFloat()
        profilePicture?.setImageResource(post.profilePicture)
        imagePost?.setImageResource(post.image_post)
    }

}
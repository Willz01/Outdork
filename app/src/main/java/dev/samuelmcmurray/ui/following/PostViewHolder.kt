package dev.samuelmcmurray.ui.following

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R

class PostViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.post_item, parent, false)) {
    private var mPosteeView: TextView? = null
    private var mDateView: TextView? = null
    private var mContentView: TextView? = null

    init {
        mPosteeView = itemView.findViewById(R.id.postee_text)
        mDateView = itemView.findViewById(R.id.date_text)
        mContentView = itemView.findViewById(R.id.content_text)
    }

    fun bind(post: Post) {
        mPosteeView?.text = post.postee
        mDateView?.text = post.date
        mContentView?.text = post.content
    }

}
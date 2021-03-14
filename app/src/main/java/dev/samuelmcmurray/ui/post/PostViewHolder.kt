package dev.samuelmcmurray.ui.post

import android.R.attr.scaleHeight
import android.R.attr.scaleWidth
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.Post
import java.io.ByteArrayOutputStream


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



    init {
        mPosterView = itemView.findViewById(R.id.postee_text)
        mDateView = itemView.findViewById(R.id.date_text)
        mContentView = itemView.findViewById(R.id.content_text)
        optionMenu = itemView.findViewById(R.id.option_menu_txt)
        like = itemView.findViewById(R.id.like_button)
        likeCount = itemView.findViewById(R.id.textViewLikeCount)
        commentCount = itemView.findViewById(R.id.textViewCommentCount)

        profilePicture = itemView.findViewById(R.id.profile_image)
        imagePost = itemView.findViewById(R.id.image_post)
    }

    fun bindWithoutImage(post: Post, context: Context) {
        mPosterView?.text = post.userName
        mDateView?.text = post.date
        mContentView?.text = post.message
        likeCount?.text = post.likes.toString()
        commentCount?.text = post.comment.toString()
        if (post.userImageURL != "nothing.com" || !post.userImageURL.equals("")) {
            Glide.with(context).load(post.userImageURL).into(profilePicture!!)
        } else {
            getBitmapProfilePicture(post.defaultProfileImage)
        }
        if (post.hasImage) {
            Glide.with(context).load(post.downloadURL).into(imagePost!!)
        } else if (post.downloadURL == "nothing.com" ) {
            Glide.with(context).load(post.image).into(imagePost!!)
        } else {
            imagePost?.visibility = View.INVISIBLE
        }
    }




    private fun getBitmapProfilePicture(src: Int){
        val newHeight = 56
        val newWidth = 56
        val originalImage = BitmapFactory.decodeResource(Resources.getSystem(), src)
        var width = originalImage.getWidth()
        Log.i(TAG,"Old width................ ${width.toString()}")
        var height = originalImage.getHeight()
        Log.i(TAG,"Old height................${height.toString()}")

        val matrix = Matrix()
        val scaleWidth = newWidth as Float / width
        val scaleHeight = newHeight as Float / height
        matrix.postScale(scaleWidth, scaleHeight)
        val resizedBitmap = Bitmap.createBitmap(originalImage, 0, 0, width, height, matrix, true)

        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        profilePicture?.setImageBitmap(resizedBitmap)
        width = resizedBitmap.width
        Log.i(TAG,"new width................ ${width.toString()}")
        height = resizedBitmap.height
        Log.i(TAG,"new height................ ${height.toString()}")
    }

}
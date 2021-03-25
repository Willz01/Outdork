package dev.samuelmcmurray.ui.favorite

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.samuelmcmurray.R
import dev.samuelmcmurray.ui.image.ImageViewModel
import dev.samuelmcmurray.ui.post.PostLocal
import dev.samuelmcmurray.utilities.ImageBitmapString

private const val TAG = "FavoriteAdapter"

class FavoriteAdapter(val context: Context, val application: Application) :
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {


    private var favorites = ArrayList<PostLocal>()
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var currentItem: PostLocal? = null


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        currentItem = favorites[position]
        Log.d(TAG, currentItem.toString())

        // TODO - currently making the option menu invisible, but might have to create a separate card item for the bookmarks fragment
        holder.itemView.findViewById<TextView>(R.id.option_menu_txt).visibility = View.INVISIBLE


        holder.itemView.findViewById<TextView>(R.id.postee_text).text =
            currentItem!!.userName
        holder.itemView.findViewById<TextView>(R.id.content_text).text =
            currentItem!!.message
        holder.itemView.findViewById<TextView>(R.id.date_text).text = currentItem!!.date
        Log.d(TAG, "getBitmapPost: ${currentItem!!.userId}")
        Log.d(TAG, "getBitmapPost: ${currentItem!!.profilePicture}")
        val profileImage = holder.itemView.findViewById<ImageView>(R.id.profile_image)
        if (currentItem!!.profileDownloadUrl.isNullOrEmpty()) {
            Glide.with(context).load(currentItem!!.profileDownloadUrl).into(profileImage)
        } else {
            val defaultProfileImage = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        application.resources.getResourcePackageName(R.drawable.defaultprofile) + '/' +
                        application.applicationContext.resources.getResourceTypeName(R.drawable.defaultprofile) + '/' +
                        R.drawable.defaultprofile.toString())
            Glide.with(context).load(defaultProfileImage).into(profileImage)
        }

        val postImage = holder.itemView.findViewById<ImageView>(R.id.image_post)
        Glide.with(context).load(currentItem!!.postDownloadUrl).centerCrop()
            .override(400, 240).into(postImage)
    }


    override fun getItemCount(): Int {
        return favorites.size
    }

    fun setFavorites(bookmarks: ArrayList<PostLocal>): List<PostLocal> {
        favoriteViewModel = FavoriteViewModel(context.applicationContext as Application)
        this.favorites = bookmarks
        notifyDataSetChanged()
        return bookmarks
    }

    fun getPostAt(position: Int) : PostLocal{
        return favorites[position]
    }

    private fun getBitmapPost(currentItem: PostLocal): Bitmap {
        val viewModel = ImageViewModel(context.applicationContext as Application)
        viewModel.getImageByFullId("${currentItem.userId}${currentItem.id}")
        val image = viewModel.imageLiveData.value
        val imageBitmapString = ImageBitmapString()
        val bitmap = imageBitmapString.StringToBitMap(image?.image!!)
        Log.d(TAG, "getBitmapPost: ${image?.fullId}")

        return bitmap!!

    }

    private fun getBitmapProfile(currentItem: PostLocal): Bitmap {
        val viewModel = ImageViewModel(context.applicationContext as Application)
        viewModel.getImageByFullId("${currentItem.userId}")
        val image = viewModel.imageLiveData.value
        val imageBitmapString = ImageBitmapString()
        val bitmap = imageBitmapString.StringToBitMap(image?.image!!)
        Log.d(TAG, "getBitmapPost: ${image?.fullId}")

        return bitmap!!

    }
}


package dev.samuelmcmurray.ui.favorite

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.ui.post.Post

private const val TAG = "FavoriteAdapter"

class FavoriteAdapter(val context: Context) :
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {


    private var favorites = ArrayList<Post>()
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var currentItem: Post? = null


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

        favoriteViewModel = FavoriteViewModel(context.applicationContext as Application)

        holder.itemView.findViewById<TextView>(R.id.postee_text).text =
            currentItem!!.poster.toString()
        holder.itemView.findViewById<TextView>(R.id.content_text).text =
            currentItem!!.content.toString()
        holder.itemView.findViewById<TextView>(R.id.date_text).text = currentItem!!.date
        holder.itemView.findViewById<ImageView>(R.id.profile_image)
            .setImageResource(currentItem!!.profilePicture)
        holder.itemView.findViewById<ImageView>(R.id.image_post)
            .setImageResource(currentItem!!.image_post)
        holder.itemView.findViewById<RatingBar>(R.id.rating).rating = currentItem!!.rating.toFloat()
    }


    override fun getItemCount(): Int {
        return favorites.size
    }

    fun setFavorites(bookmarks: ArrayList<Post>): List<Post> {
        this.favorites = bookmarks
        notifyDataSetChanged()
        return bookmarks
    }

    fun getPostAt(position: Int) : Post{
        return favorites[position]
    }

}


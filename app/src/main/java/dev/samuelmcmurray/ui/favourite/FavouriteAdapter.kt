package dev.samuelmcmurray.ui.favourite

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.ui.post.Post

private const val TAG = "FavouriteAdapter"

class FavouriteAdapter(val context: Context) :
    RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {


    private var favourites = ArrayList<Post>()
    private lateinit var favouriteViewModel: FavouriteViewModel
    private var currentItem: Post? = null


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        currentItem = favourites[position]
        Log.d(TAG, currentItem.toString())

        // TODO - currently making the option menu invisible, but might have to create a separate card item for the bookmarks fragment
        holder.itemView.findViewById<TextView>(R.id.option_menu_txt).visibility = View.INVISIBLE

        favouriteViewModel = FavouriteViewModel(context.applicationContext as Application)

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
        return favourites.size
    }

    fun setFavourites(bookmarks: ArrayList<Post>): List<Post> {
        this.favourites = bookmarks
        notifyDataSetChanged()
        return bookmarks
    }

    val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            Log.d(TAG, "onSwiped: ${viewHolder.adapterPosition}")
            Log.d(TAG, currentItem.toString())
            val post = Post(
                0,
                "45444f4f",
                R.drawable.hiker_pp1,
                R.drawable.hike_image1,
                5.0,
                "Mr Darcy",
                "21/20/11",
                "this is a post"
            )
            favouriteViewModel = FavouriteViewModel(context.applicationContext as Application)

            deleteFromFavourites(favourites[viewHolder.bindingAdapterPosition])
            notifyDataSetChanged()
        }

    }

    /* inner class ItemHelper(context: Context) :
         ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

         // for move operation -> moving the card in the rv
         override fun onMove(
             recyclerView: RecyclerView,
             viewHolder: RecyclerView.ViewHolder,
             target: RecyclerView.ViewHolder
         ): Boolean {
             TODO("Not yet implemented")
         }

         override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
             val position = viewHolder.adapterPosition
             Log.d("Adapter", position.toString())
             val post = favourites[0]
             deleteFromFavourites(post)
             notifyDataSetChanged()
         }
     }*/

    fun deleteFromFavourites(post: Post) {
        favouriteViewModel.removePost(post)
    }

}
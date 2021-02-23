package dev.samuelmcmurray.ui.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.ui.post.Post

class FavouriteAdapter : RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {

    private var favourites = emptyList<Post>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = favourites[position]
        // TODO - currently making the option menu invisible, but might have to create a separate card item for the bookmarks fragment
        holder.itemView.findViewById<TextView>(R.id.option_menu_txt).visibility = View.INVISIBLE

        holder.itemView.findViewById<TextView>(R.id.postee_text).text = currentItem.poster.toString()
        holder.itemView.findViewById<TextView>(R.id.content_text).text = currentItem.content.toString()
        holder.itemView.findViewById<TextView>(R.id.date_text).text = currentItem.date

    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    fun setFavourites(bookmarks : List<Post>){
        this.favourites = bookmarks
        notifyDataSetChanged()
    }
}
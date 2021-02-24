package dev.samuelmcmurray.ui.post

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.ui.favourite.FavouriteViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class PostAdapter(private val list: List<Post>, var context: Context) :
    RecyclerView.Adapter<PostViewHolder>() {

    private lateinit var bookmarksViewModel: FavouriteViewModel

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
        holder.bind(post)


        // change post rating on rating bar change
        holder.itemView.findViewById<RatingBar>(R.id.rating).onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ -> post.rating = BigDecimal(rating.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toDouble() }

        holder.itemView.findViewById<TextView>(R.id.option_menu_txt).setOnClickListener {
            val popupMenu = PopupMenu(context, holder.itemView.findViewById(R.id.option_menu_txt))
            popupMenu.inflate(R.menu.post_option_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                bookmarksViewModel =
                    FavouriteViewModel(context.applicationContext as Application)

                if (item != null) {
                    when (item.itemId) {
                        R.id.favourite -> {
                            addToBookmarks(post)
                            Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT)
                                .show()
                        }
                        R.id.share -> Toast.makeText(
                            context,
                            "Time to share to facebook",
                            Toast.LENGTH_SHORT
                        ).show()
                        R.id.report -> Toast.makeText(context, "Reported", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                false
            }
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int = list.size


    private fun addToBookmarks(post: Post) {
        bookmarksViewModel.addPost(post)
    }


}
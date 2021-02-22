package dev.samuelmcmurray.ui.post

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.ui.favourite.FavouriteViewModel

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
        holder.itemView.findViewById<TextView>(R.id.option_menu_txt).setOnClickListener {
            val popupMenu = PopupMenu(context, holder.itemView.findViewById(R.id.option_menu_txt))
            popupMenu.inflate(R.menu.post_option_menu)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {

                    bookmarksViewModel = FavouriteViewModel(context.applicationContext as Application)

                    if (item != null) {
                        when (item.itemId) {
                            R.id.favourite -> {
                                val userName = post.poster.toString()
                                val date = post.date
                                val content = post.content
                                addToBookmarks(userName, date, content)
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
                    return false
                }

            })
            popupMenu.show()
        }
    }

    override fun getItemCount(): Int = list.size


    private fun addToBookmarks(userName: String, date: String, content: String) {
        val post = Post(0, userName, date, content)
        bookmarksViewModel.addPost(post)
    }


}
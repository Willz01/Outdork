package dev.samuelmcmurray.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentFavouriteBinding
import dev.samuelmcmurray.ui.post.Post


class FavouriteFragment : Fragment() {


    companion object {
        fun newInstance() = FavouriteFragment()
    }

    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var viewModel: FavouriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false)
        binding.lifecycleOwner = this

        val adapter = FavouriteAdapter(requireContext())
        val recyclerView = binding.bookmarksRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = adapter

        // view model
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)
        viewModel.readAllFavourites.observe(
            viewLifecycleOwner,
            Observer { posts -> adapter.setFavourites(posts as ArrayList<Post>) })

        // item swipe helper : swipe to un favorite
        val touchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.removePost(adapter.getPostAt(viewHolder.adapterPosition))
                Toast.makeText(requireContext(), "Removed", Toast.LENGTH_SHORT).show()
            }

        }
        ItemTouchHelper(touchHelper).attachToRecyclerView(recyclerView)
        return binding.root
    }

    /*  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
          super.onViewCreated(view, savedInstanceState)
          val factory = InjectorUtils.provideBookmarksViewModelFactory()
          viewModel = ViewModelProvider(this, factory).get(FavouriteViewModel::class.java)
      }*/
}
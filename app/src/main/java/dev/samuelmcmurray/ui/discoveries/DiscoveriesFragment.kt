package dev.samuelmcmurray.ui.discoveries

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.databinding.DiscoveriesFragmentBinding
import dev.samuelmcmurray.ui.post.Post
import dev.samuelmcmurray.ui.post.PostAdapter

private val posts = listOf(
    Post(
        0,
        R.drawable.hiker_pp1,
        R.drawable.hike_image1,
        5.0,
        "Mr Darcy",
        "21/20/11",
        "Great work at Hammar today,"
    ),
    Post(
        0,
        R.drawable.hiker_pp2,
        R.drawable.hike_image2,
        3.7,
        "superhiker2324",
        "19/55/62",
        "hello another post"
    ),
    Post(
        0,
        R.drawable.hiker_pp3,
        R.drawable.hike_image3,
        3.3,
        "mY dOg",
        "14/56/95",
        "another poist"
    ),
    Post(
        0,
        R.drawable.hiker_pp4,
        R.drawable.hike_image4,
        2.6,
        "Superman",
        "21/15/13",
        "the last post"
    )
)

private const val TAG = "DiscoveriesFragment"

class DiscoveriesFragment : Fragment() {

    companion object {
        fun newInstance() = DiscoveriesFragment()
    }

    private lateinit var binding: DiscoveriesFragmentBinding
    private lateinit var viewModel: DiscoveriesViewModel
    private lateinit var fragment: View
    lateinit var postTextView : MaterialTextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.discoveries_fragment, container, false)
        binding.lifecycleOwner = this
        fragment =  binding.postFragment
        postTextView = binding.editTextPost
        fragment.visibility = View.GONE
        postTextView.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),defaultViewModelProviderFactory).get(DiscoveriesViewModel::class.java)
        val recyclerview = binding.root.findViewById<RecyclerView>(R.id.recycler_view_discoveries)
        getCurrentUser()
        recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = PostAdapter(posts, requireContext())
        }

        postTextView.setOnClickListener {
            postTextView.visibility = View.GONE
            fragment.visibility = View.VISIBLE
            viewModel.newPostVisibilityLiveData.observe(viewLifecycleOwner, Observer {
                if (fragment.isGone) {
                    postTextView.visibility = View.VISIBLE
                }
            })
        }

    }

    private fun getCurrentUser() {
        viewModel.getCurrentUser()
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            val currentUser = it
            if (currentUser != null) {

                Log.d(TAG, "currentUser success: " )
            }
        })
    }
}
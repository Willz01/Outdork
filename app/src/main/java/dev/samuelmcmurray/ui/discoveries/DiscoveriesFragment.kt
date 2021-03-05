package dev.samuelmcmurray.ui.discoveries

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.databinding.DiscoveriesFragmentBinding
import dev.samuelmcmurray.ui.post.Post
import dev.samuelmcmurray.ui.post.PostAdapter

private val posts = listOf(
    Post(
        0,
        "45444f4f",
        R.drawable.hiker_pp1,
        R.drawable.hike_image1,
        5.0,
        "Mr Darcy",
        "21/20/11",
        "this is a post"
    ),
    Post(
        0,
        "erefe22e",
        R.drawable.hiker_pp2,
        R.drawable.hike_image2,
        3.7,
        "superhiker2324",
        "19/55/62",
        "hello another post"
    ),
    Post(
        0,
        "er3e3d3e",
        R.drawable.hiker_pp3,
        R.drawable.hike_image3,
        3.3,
        "mY dOg",
        "14/56/95",
        "another poist"
    ),
    Post(
        0,
        "efr3d3d33",
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
    private lateinit var floatingActionButton: FloatingActionButton
    lateinit var postTextView : MaterialTextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.discoveries_fragment, container, false)
        binding.lifecycleOwner = this
        fragment =  binding.postFragment
        postTextView = binding.editTextPost
        floatingActionButton = binding.floatingActionButton
        fragment.visibility = View.GONE
        postTextView.visibility = View.VISIBLE
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),defaultViewModelProviderFactory).get(DiscoveriesViewModel::class.java)
        getCurrentUser()
        val recyclerview = binding.root.findViewById<RecyclerView>(R.id.recycler_view_discoveries)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = PostAdapter(posts, requireContext())
        }

        floatingActionButton.setOnClickListener{
            showHide()
        }

        postTextView.setOnClickListener {
            showHide()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentUser() {
        if (CurrentUserSingleton.getInstance.loggedIn || CurrentUserSingleton.getInstance.currentUser == null) {
            viewModel.getCurrentUser()
            viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
                val currentUser = it
                if (currentUser != null) {
                    Log.d(TAG, "currentUser success: ")
                } else {
                    Log.d(TAG, "getCurrentUser: failure")
                }
            })
        }
    }

    private fun showHide(){
        postTextView.visibility = View.GONE
        fragment.visibility = View.VISIBLE
        viewModel.hideNewPostFragment(false)
        viewModel.hideBoolean.observe(viewLifecycleOwner, Observer {
            if (it) {
                postTextView.visibility = View.VISIBLE
                fragment.visibility = View.GONE
                floatingActionButton.show()
            } else {
                postTextView.visibility = View.GONE
                fragment.visibility = View.VISIBLE
                floatingActionButton.hide()
            }
        })
    }

}
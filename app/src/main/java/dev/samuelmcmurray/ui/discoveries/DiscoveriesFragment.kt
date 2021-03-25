package dev.samuelmcmurray.ui.discoveries

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import de.hdodenhof.circleimageview.CircleImageView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.databinding.DiscoveriesFragmentBinding
import dev.samuelmcmurray.ui.post.PostAdapter
import dev.samuelmcmurray.utilities.AppGlideModule
import dev.samuelmcmurray.utilities.GlideApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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

        viewModel.getPostsList()
        viewModel.postsListLiveData.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()) {
                val recyclerview = binding.root.findViewById<RecyclerView>(R.id.recycler_view_discoveries)
                recyclerview.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = PostAdapter(list, requireContext())

                }
            }
        })

        viewModel.viewOtherProfileLiveData.observe(viewLifecycleOwner, Observer { viewProfile ->
            Log.d(TAG, "onViewCreated: $viewProfile")
            if (viewProfile) {
                val navHostFragment =
                    requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
                val navController = navHostFragment.navController
                viewModel.viewOtherProfile(false)
                navController.navigate(R.id.otherProfileFragment)
            }
        })

        getCurrentUser()
        CoroutineScope(Dispatchers.Main).launch {
            loadImagesAndUserName()
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
                parentFragmentManager.beginTransaction().detach(this)
                    .attach(this).commit()
                floatingActionButton.show()
            } else {
                postTextView.visibility = View.GONE
                fragment.visibility = View.VISIBLE
                floatingActionButton.hide()
            }
        })
    }

    private suspend fun loadImagesAndUserName() {
        while (CurrentUserSingleton.getInstance.currentUser == null){
            delay(1000)
        }
        val navigationView = requireActivity().findViewById(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        try {

            val navPicture =
                headerView.findViewById<View>(R.id.profilePicture) as ImageView
            if (CurrentUserSingleton.getInstance.currentUser!!.hasImage) {
                val imageResource = CurrentUserSingleton.getInstance.currentUser!!.profilePhoto
                context?.let { GlideApp.with(it.applicationContext).load(imageResource).override(100).into(navPicture) }
            } else {
                val imageResource = Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                            requireActivity().resources.getResourcePackageName(R.drawable.defaultprofile) + '/' +
                            requireActivity().resources.getResourceTypeName(R.drawable.defaultprofile) + '/' +
                            R.drawable.defaultprofile.toString())
                context?.let {
                    GlideApp.with(it.applicationContext).load(imageResource).override(100).into(navPicture)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
        val navUsername =
            headerView.findViewById<View>(R.id.profileName) as TextView
        navUsername.text = CurrentUserSingleton.getInstance.currentUser!!.userName
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
////        try {
        val navEmail =
            headerView.findViewById<View>(R.id.profileEmail) as TextView
        navEmail.text = CurrentUserSingleton.getInstance.currentUser!!.email
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
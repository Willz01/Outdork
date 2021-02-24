package dev.samuelmcmurray.ui.following.feeds

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.share.widget.ShareButton
import com.facebook.share.widget.ShareDialog
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentFollowingBinding
import dev.samuelmcmurray.ui.post.Post
import dev.samuelmcmurray.ui.post.PostAdapter
import dev.samuelmcmurray.utilities.InjectorUtils


class FollowingFeedsFragment : Fragment() {

    private var shareButton: ShareButton? = null
    private lateinit var callbackManager: CallbackManager

    private val posts = listOf(
        Post(0,"Mr Darcy", "21/20/11", "this is a post"),
        Post(0,"superhiker2324", "19/55/62", "hello another post"),
        Post(0,"mY dOg", "14/56/95", "another poist"),
        Post(0,"Superman", "21/15/13", "the last post"),
    )

    companion object {
        fun newInstance() = FollowingFeedsFragment()
    }

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var viewModel: FollowingFeedsViewModel

    private lateinit var shareDialog: ShareDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false)
        binding.lifecycleOwner = this

        FacebookSdk.sdkInitialize(this.requireContext())

        // used is logged in
        val loggedIn = AccessToken.getCurrentAccessToken() != null
        println(loggedIn)

        /*shareButton = requireActivity().findViewById(R.id.share_button)
        callbackManager = CallbackManager.Factory.create()*/


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideFollowingViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(FollowingFeedsViewModel::class.java)

        val recyclerview = binding.root.findViewById<RecyclerView>(R.id.recycler_view_following)
        recyclerview.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = PostAdapter(posts, requireContext())
        }
    }
    //viewModel.getAbout().observe(this, Observer {what ever we do})


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*callbackManager.onActivityResult(requestCode, resultCode, data)

        // test case with a link
        val shareLinkContent = ShareLinkContent.Builder()
            .setContentUrl(Uri.parse("https://www.youtube.com/c/MichaelSambol/videos"))
            .setShareHashtag(ShareHashtag.Builder().setHashtag("#TEST").build()).build()

        shareButton!!.shareContent = shareLinkContent*/


    }
}
package dev.samuelmcmurray.ui.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentFollowingBinding
import dev.samuelmcmurray.utilities.InjectorUtils


class FollowingFragment : Fragment() {

    private val posts = listOf(
        Post("Mr Darcy", "21/20/11", "this is a post"),
        Post("superhiker2324", "19/55/62", "hello another post"),
        Post("mY dOg", "14/56/95", "another poist"),
        Post("Superman", "21/15/13", "the last post"),
    )

    companion object {
        fun newInstance() = FollowingFragment()
    }
    private lateinit var binding : FragmentFollowingBinding
    private lateinit var viewModel : FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_following, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideFollowingViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(FollowingViewModel::class.java)

        val recyclerview = binding.root.findViewById<RecyclerView>(R.id.recycler_view_following)
        recyclerview.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            layoutManager = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            adapter = PostAdapter(posts)
        }
    }
        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }
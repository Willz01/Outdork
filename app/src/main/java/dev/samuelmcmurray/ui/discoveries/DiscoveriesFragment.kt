package dev.samuelmcmurray.ui.discoveries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.DiscoveriesFragmentBinding
import dev.samuelmcmurray.ui.post.Post
import dev.samuelmcmurray.ui.post.PostAdapter
import dev.samuelmcmurray.utilities.InjectorUtils

private val posts = listOf(
    Post(0, "Mr Darcy", "21/20/11", "this is a post"),
    Post(0, "superhiker2324", "19/55/62", "hello another post"),
    Post(0, "mY dOg", "14/56/95", "another poist"),
    Post(0, "Superman", "21/15/13", "the last post"),
)

class DiscoveriesFragment : Fragment() {

    companion object {
        fun newInstance() = DiscoveriesFragment()
    }

    private lateinit var binding: DiscoveriesFragmentBinding
    private lateinit var viewModel: DiscoveriesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.discoveries_fragment, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideDiscoveriesViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(DiscoveriesViewModel::class.java)

        val recyclerview = binding.root.findViewById<RecyclerView>(R.id.recycler_view_discoveries)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = PostAdapter(posts, requireContext())
        }


        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }

}
package dev.samuelmcmurray.ui.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentOtherProfileBinding
import dev.samuelmcmurray.ui.post.PostAdapter
import dev.samuelmcmurray.utilities.InjectorUtils

class OtherProfilePostsFragment : Fragment() {

    companion object {
        fun newInstance() = OtherProfilePostsFragment()
    }

    private lateinit var binding: FragmentOtherProfileBinding
    private lateinit var viewModel: OtherProfileViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_other_profile, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideFollowingViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(OtherProfileViewModel::class.java)

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
    }
}
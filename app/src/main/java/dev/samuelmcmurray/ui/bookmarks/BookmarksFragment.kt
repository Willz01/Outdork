package dev.samuelmcmurray.ui.bookmarks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentBookmarksBinding
import dev.samuelmcmurray.utilities.InjectorUtils


class BookmarksFragment : Fragment() {

    companion object {
        fun newInstance() = BookmarksFragment()
    }
    private lateinit var binding : FragmentBookmarksBinding
    private lateinit var viewModel: BookmarksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmarks, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideBookmarksViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(BookmarksViewModel::class.java)
    }
}
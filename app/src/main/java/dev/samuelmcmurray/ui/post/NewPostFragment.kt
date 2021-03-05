package dev.samuelmcmurray.ui.post

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Build

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentNewPostBinding
import dev.samuelmcmurray.ui.discoveries.DiscoveriesViewModel

private const val TAG = "NewPostFragment"
class NewPostFragment : Fragment() {
    companion object {
        fun newInstance() = NewPostFragment()
    }


    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
        if (uri != null ) {
            Log.d(TAG, ": $uri")
            viewModel.onGetImage(uri)
        } else {
            Toast.makeText(requireContext(), "Photo not found", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            viewModel.onGetImage(image)
        } else {
            Toast.makeText(requireContext(), "Photo failed", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { success ->
        if (success) {
            takePicture.launch(image)
        } else {
            Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var binding: FragmentNewPostBinding
    private lateinit var viewModel: DiscoveriesViewModel
    private var image : Uri = Uri.EMPTY


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_post, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(),defaultViewModelProviderFactory).get(DiscoveriesViewModel::class.java)
        val cancelButton = binding.buttonCancel
        val postButton = binding.buttonPost
        val postText = binding.editTextPost
        val photo = binding.imageGallery
        val camera = binding.imageCamera

        cancelButton.setOnClickListener {
            hideKeyboard()
            viewModel.hideNewPostFragment(true)
        }

        photo.setOnClickListener {
            getImage.launch("image/*")

        }

        camera.setOnClickListener {
            requestPermission.launch(Manifest.permission.CAMERA)
        }

        postButton.setOnClickListener {
            val message = postText.text.toString()
            if (message.isEmpty()) {
                Toast.makeText(requireContext(), "The post can not have an empty textfield", Toast.LENGTH_SHORT).show()
            } else {
                post(message)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun post(message: String) {
        viewModel.newPost(message)
        viewModel.postCreatedLiveData.observe(viewLifecycleOwner, Observer {
            val created = it
            if (created) {
                viewModel.hideNewPostFragment(true)
                Toast.makeText(requireContext(), "Posted", Toast.LENGTH_SHORT).show()
                hideKeyboard()
                binding.root.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(), "Failed to Post", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}
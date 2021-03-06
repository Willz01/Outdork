package dev.samuelmcmurray.ui.post

import android.Manifest
import android.app.Activity
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.BuildConfig
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentNewPostBinding
import dev.samuelmcmurray.ui.discoveries.DiscoveriesViewModel
import java.io.File

private const val TAG = "NewPostFragment"
class NewPostFragment : Fragment() {

    private lateinit var imageUri: Uri
    private lateinit var binding: FragmentNewPostBinding
    private lateinit var viewModel: DiscoveriesViewModel

    companion object {
        fun newInstance() = NewPostFragment()
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
        if (uri != null ) {
            Log.d(TAG, ": $uri")
            val bitmap = when {
                Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    uri
                )
                else -> {
                    val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
            }
            viewModel.onGetImage(uri)
        } else {
            Toast.makeText(requireContext(), "Photo not found", Toast.LENGTH_SHORT).show()
        }
    }


    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            viewModel.onGetImage(imageUri)
        } else {
            Toast.makeText(requireContext(), "Photo failed", Toast.LENGTH_SHORT).show()
        }
    }


    private val requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { success ->
        if (success) {
            takePicture.launch(getCameraCacheUri())
        } else {
            Toast.makeText(requireContext(), "Permissions denied", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_post, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
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
            getImageContent.launch("image/*")

        }

        camera.setOnClickListener {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
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

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun getCameraCacheUri(): Uri {
        val cameraCachePath = File(requireContext().cacheDir, "camera")
        Log.d(TAG, "getCameraCacheUri: $cameraCachePath")
        if (!cameraCachePath.exists()) {
            cameraCachePath.mkdirs()
        }

        val filename = "picture.jpg"
        val file = File(cameraCachePath, filename)
        imageUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", file)
        return imageUri
    }

}
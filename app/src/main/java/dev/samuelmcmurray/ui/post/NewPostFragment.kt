package dev.samuelmcmurray.ui.post

import android.Manifest
import android.net.Uri

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentNewPostBinding
import dev.samuelmcmurray.ui.discoveries.DiscoveriesViewModel


class NewPostFragment : Fragment() {
    companion object {
        fun newInstance() = NewPostFragment
    }


    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri : Uri? ->
        if (uri != null ) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(),defaultViewModelProviderFactory).get(DiscoveriesViewModel::class.java)
        val cancelButton = binding.buttonCancel
        val photo = binding.imageGallery
        val camera = binding.imageCamera

        cancelButton.setOnClickListener {
            binding.root.visibility = View.GONE

        }

        photo.setOnClickListener {
            getImage.launch("image/*")

        }

        camera.setOnClickListener {
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }

}
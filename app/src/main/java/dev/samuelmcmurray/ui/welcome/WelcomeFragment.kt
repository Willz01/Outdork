package dev.samuelmcmurray.ui.welcome

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentWelcomeBinding


private const val TAG = "WelcomeFragment"
class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, R.layout.fragment_welcome, container, false)
        binding.lifecycleOwner = this

        val progressBar = binding.progressBar
        progressBar.progress
        return binding.root
    }
    /*
    overide fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigationView = requireActivity().findViewById(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val navPicture =
            headerView.findViewById<View>(R.id.profilePicture) as ImageView
        // future when I can get profile picture
        //navPicture.setImageResource()
        val navUsername =
            headerView.findViewById<View>(R.id.profileName) as TextView
        // future when I can get profile data
        navUsername.text = "Username"
        val navEmail =
            headerView.findViewById<View>(R.id.profileEmail) as TextView
        navEmail.text = "Email"
        // future when I can get profile data

*/
    }





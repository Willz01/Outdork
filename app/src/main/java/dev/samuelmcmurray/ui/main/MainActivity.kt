package dev.samuelmcmurray.ui.main

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.customview.widget.Openable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.facebook.AccessToken
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var bottomNavigationView: BottomNavigationView

    /**
     * selectedFilter --> Used to keep track of selected filter options in {@link NewActivityFragment}
     * startLocation  --> Used for origin location for direction routing in {@link MapsFragment and SelectRouteFragment}
     * Nav args is also used as well
     */
    companion object{
        fun newInstance() = MainActivity()
        var selectedFilter = ArrayList<String>()
        var startLocation = String()
        var latLng : Address? = null
        var alreadyFetchedLocation = false
        var mMap : GoogleMap? = null
    }

    private lateinit var mNavView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        drawerLayout = binding.drawerLayout
        // add all new fragments here!
        appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.followingFragment,
                R.id.discoveries_fragment,
                R.id.aboutFragment,
                R.id.addNewActivityFragment,
                R.id.favouriteFragment,
                R.id.helpFragment,
                R.id.newActivityFragment,
                R.id.reportFragment,
                R.id.settingsFragment,
                R.id.followingHolder,
                R.id.followingListFragment, R.id.mapsFragment,
                R.id.followingListFragment,
                R.id.profileFragment
            )
        ).setOpenableLayout(drawerLayout as Openable).build()

        bottomNavigationView = findViewById(R.id.nav)

        binding.navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.welcomeFragment
                || destination.id == R.id.registerFragment || destination.id == R.id.forgotPasswordFragment
            ) {
                supportActionBar?.hide()
                bottomNavigationView.visibility = View.GONE
            } else {
                supportActionBar?.show()
                bottomNavigationView.visibility = View.VISIBLE
            }
        }

        mNavView = findViewById(R.id.nav_view)
        val header = mNavView.getHeaderView(0)
        val profilePic: ImageButton = header.findViewById(R.id.profilePicture)
        profilePic.setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }

//        val profileImage: CircleImageView = header.findViewById(R.id.profile_image)
//        profileImage.setOnClickListener {
//            navController.navigate(R.id.otherProfileHolder)
//        }

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                navController.navigate(R.id.loginFragment)
            } else {
                hideKeyboard()
                navController.navigate(R.id.discoveries_fragment)
            }
        }

        val loggedIn = AccessToken.getCurrentAccessToken() != null
        println(loggedIn)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //super.onActivityResult(requestCode, resultCode, data);
        try {
            for (fragment in supportFragmentManager.fragments) {
                fragment.onActivityResult(requestCode, resultCode, data)
                Log.d("Activity", "ON RESULT CALLED")
            }
        } catch (e: Exception) {
            Log.d("ERROR", e.toString())
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

}
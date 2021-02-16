package dev.samuelmcmurray.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.customview.widget.Openable
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragment)
        drawerLayout = binding.drawerLayout
        // add all new fragments here!
        appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.followingFragment,
                R.id.discoveries_fragment,
                R.id.aboutFragment,
                R.id.addNewActivityFragment,
                R.id.bookmarksFragment,
                R.id.favouritesFragment,
                R.id.helpFragment,
                R.id.newActivityFragment,
                R.id.reportFragment,
                R.id.settingsFragment
            )
        ).setOpenableLayout(drawerLayout as Openable).build()

        bottomNavigationView = findViewById(R.id.nav)


        binding.navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        fragmentManager?.popBackStack()
        super.onBackPressed()
    }

}
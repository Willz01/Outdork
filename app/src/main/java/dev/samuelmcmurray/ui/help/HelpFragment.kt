package dev.samuelmcmurray.ui.help

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentHelpBinding
import dev.samuelmcmurray.utilities.InjectorUtils


class HelpFragment : Fragment() {

    companion object {
        fun newInstance() = HelpFragment()
    }

    private lateinit var binding : FragmentHelpBinding
    private lateinit var viewModel : HelpViewModel
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val drawerLayout : DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_help, container, false)
        binding.lifecycleOwner = this
        // test
        navController = requireActivity().findNavController(R.id.fragmentContainer)
        bottomNavigationView = requireActivity().findViewById(R.id.nav)
        bottomNavigationView.visibility = View.GONE

        val view = binding.root

        val favoritesButton = view.findViewById<Button>(R.id.favoritesButton)
        val followingButton = view.findViewById<Button>(R.id.followingButton)
        val findActivityButton = view.findViewById<Button>(R.id.findActivityButton)
        val discoveriesButton = view.findViewById<Button>(R.id.discoveriesButton)

        val favoritesText = view.findViewById<TextView>(R.id.favoritesText)
        val followingText = view.findViewById<TextView>(R.id.followingText)
        val findActivityText = view.findViewById<TextView>(R.id.findActivityText)
        val discoveriesText = view.findViewById<TextView>(R.id.discoveriesText)

        setText(favoritesText, followingText, findActivityText, discoveriesText)

        favoritesText.visibility = TextView.INVISIBLE
        followingText.visibility = TextView.INVISIBLE
        findActivityText.visibility = TextView.INVISIBLE
        discoveriesText.visibility = TextView.INVISIBLE

        favoritesText.movementMethod = ScrollingMovementMethod()
        followingText.movementMethod = ScrollingMovementMethod()
        findActivityText.movementMethod = ScrollingMovementMethod()
        discoveriesText.movementMethod = ScrollingMovementMethod()

            favoritesButton.setOnClickListener() {
                favoritesText.visibility = if (favoritesText.visibility == TextView.VISIBLE){
                    TextView.INVISIBLE
                } else{
                    TextView.VISIBLE
                }

            }

            followingButton.setOnClickListener() {
                followingText.visibility = if (followingText.visibility == TextView.VISIBLE){
                    TextView.INVISIBLE
                } else{
                    TextView.VISIBLE
                }

            }

            findActivityButton.setOnClickListener() {
                findActivityText.visibility = if (findActivityText.visibility == TextView.VISIBLE){
                    TextView.INVISIBLE
                } else{
                    TextView.VISIBLE
                }

            }


            discoveriesButton.setOnClickListener() {
                discoveriesText.visibility = if (discoveriesText.visibility == TextView.VISIBLE){
                    TextView.INVISIBLE
                } else{
                    TextView.VISIBLE
                }

            }



        return view
    }

    // testing out hiding the bottom nav bar in screen/fragments we don't need them -> works!
    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideHelpViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(HelpViewModel::class.java)


        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }

    private fun setText(favoritesText:TextView, followingText:TextView, findActivityText:TextView, discoveriesText:TextView) {
        favoritesText.text = """In the favorites section, you can see all your favorite trails and activities, 
                |a good way to always keep your best activities handy. 
                |
                |In every post and activity, you can choose to favorite it and if you change your mind, you can always
                |swipe to the right to remove it from the list. 
    
            """.trimMargin()

        followingText.text = """In the follwing section, you can see all the people you follow.
            |The feed tab shows you all posts made by the people you follow. 
            |The following tab shows you a list of all the people you follow, so you can easily keep track. 
            |Make sure to like the posts!
               
            """.trimMargin()
        findActivityText.text = """The feature "Find Activity" helps you to find the right activity then you are looking for. 
            |You choose the activities you are looking for in you outdoor adventure and then you can make additional filtering 
            |amongst the many activities there are. 
            |Make sure to look at the rating before you decide to out in the wild!
            """.trimMargin()
        discoveriesText.text = """In here you can discover new people to follow or even activities you would never search for. 
            |It shows you activities all around the world and give you inspiration for your next adventure. 
            |Browse away
            """.trimMargin()

    }
}

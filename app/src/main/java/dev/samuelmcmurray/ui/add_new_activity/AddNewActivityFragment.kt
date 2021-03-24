package dev.samuelmcmurray.ui.add_new_activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.Activity
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.databinding.FragmentAddNewBinding
import dev.samuelmcmurray.sampleactivities.Activities

class AddNewActivityFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewActivityFragment()
    }

    private lateinit var binding: FragmentAddNewBinding
    private lateinit var viewModelProvider: AddNewActivityViewModel
    private lateinit var viewModel: AddNewActivityViewModel

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navController = requireActivity().findNavController(R.id.fragmentContainer)
        bottomNavigationView = requireActivity().findViewById(R.id.nav)
        bottomNavigationView.visibility = View.GONE
        return inflater.inflate(R.layout.fragment_add_new, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*   val factory = InjectorUtils.provideAddNewActivityViewModelFactory()
           viewModelProvider = ViewModelProvider(this, factory).get(AddNewActivityViewModel::class.java)
           viewModel = viewModelProvider*/

        val activityNameTV = view.findViewById<TextInputEditText>(R.id.activity_name_new)
        val latitude = view.findViewById<TextInputEditText>(R.id.latitude)
        val longitude = view.findViewById<TextInputEditText>(R.id.longitude)

        val listActivities = ArrayList<String>()
        view.findViewById<Button>(R.id.submit_activity).setOnClickListener {
            if (view.findViewById<CheckBox>(R.id.bikingCB).isChecked) {
                listActivities.add(view.findViewById<CheckBox>(R.id.bikingCB).text as String)
            }
            if (view.findViewById<CheckBox>(R.id.hikingCB).isChecked) {
                listActivities.add(view.findViewById<CheckBox>(R.id.hikingCB).text as String)
            }
            if (view.findViewById<CheckBox>(R.id.dogWalkingCB).isChecked) {
                listActivities.add(view.findViewById<CheckBox>(R.id.dogWalkingCB).text as String)
            }
            if (view.findViewById<CheckBox>(R.id.horseRidingCB).isChecked) {
                listActivities.add(view.findViewById<CheckBox>(R.id.horseRidingCB).text as String)
            }
            if (view.findViewById<CheckBox>(R.id.swimmingCB).isChecked) {
                listActivities.add(view.findViewById<CheckBox>(R.id.swimmingCB).text as String)
            }
            if (view.findViewById<CheckBox>(R.id.fishingCB).isChecked) {
                listActivities.add(view.findViewById<CheckBox>(R.id.fishingCB).text as String)
            }
            if (view.findViewById<CheckBox>(R.id.birdWatchingCB).isChecked) {
                listActivities.add(view.findViewById<CheckBox>(R.id.birdWatchingCB).text as String)
            }
            if (view.findViewById<CheckBox>(R.id.scenicCB).isChecked) {
                listActivities.add(view.findViewById<CheckBox>(R.id.birdWatchingCB).text as String)
            }

            val user = CurrentUserSingleton.getInstance.currentUser?.id
            val lat = latitude.text.toString().toDouble()
            val long = longitude.text.toString().toDouble()
            // using a default rating for now
            val activity =
                Activity(activityNameTV.text.toString(), user!!, listActivities, LatLng(lat, long), 3.9)

            Activities.addActivity(activity)
            activityNameTV.text = null
            latitude.text = null
            longitude.text = null

            view.findViewById<CheckBox>(R.id.bikingCB).isChecked = false
            view.findViewById<CheckBox>(R.id.hikingCB).isChecked = false
            view.findViewById<CheckBox>(R.id.dogWalkingCB).isChecked = false
            view.findViewById<CheckBox>(R.id.horseRidingCB).isChecked = false
            view.findViewById<CheckBox>(R.id.swimmingCB).isChecked = false
            view.findViewById<CheckBox>(R.id.fishingCB).isChecked = false
            view.findViewById<CheckBox>(R.id.birdWatchingCB).isChecked = false
            view.findViewById<CheckBox>(R.id.scenicCB).isChecked = false


        }


    }
}
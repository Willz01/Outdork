package dev.samuelmcmurray.ui.add_new_activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.data.model.Activity
import dev.samuelmcmurray.data.singelton.CurrentUserSingleton
import dev.samuelmcmurray.databinding.FragmentAddNewBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new, container, false)

        navController = requireActivity().findNavController(R.id.fragmentContainer)
        bottomNavigationView = requireActivity().findViewById(R.id.nav)
        bottomNavigationView.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*   val factory = InjectorUtils.provideAddNewActivityViewModelFactory()
           viewModelProvider = ViewModelProvider(this, factory).get(AddNewActivityViewModel::class.java)
           viewModel = viewModelProvider*/

        val activityNameTV = binding.activityName
        val latitude = binding.latitude
        val longitude = binding.longitude

        val listActivities = ArrayList<String>()
        binding.submitActivity.setOnClickListener {
            if (binding.bikingCB.isChecked) {
                listActivities.add(binding.bikingCB.text as String)
            }
            if (binding.hikingCB.isChecked) {
                listActivities.add(binding.hikingCB.text as String)
            }
            if (binding.dogWalkingCB.isChecked) {
                listActivities.add(binding.dogWalkingCB.text as String)
            }
            if (binding.horseRidingCB.isChecked) {
                listActivities.add(binding.horseRidingCB.text as String)
            }
            if (binding.swimmingCB.isChecked) {
                listActivities.add(binding.swimmingCB.text as String)
            }
            if (binding.fishingCB.isChecked) {
                listActivities.add(binding.fishingCB.text as String)
            }
            if (binding.birdWatchingCB.isChecked) {
                listActivities.add(binding.birdWatchingCB.text as String)
            }
            if (binding.scenicCB.isChecked) {
                listActivities.add(binding.scenicCB.text as String)
            }
        }

        val user = CurrentUserSingleton.getInstance.currentUser?.id
        val lat = latitude.text.toString().toDouble()
        val long = longitude.text.toString().toDouble()
        // using a default rating for now
        val activity =
            Activity(activityNameTV.text.toString(), user!!, listActivities, LatLng(lat, long), 3.9)
    }
}
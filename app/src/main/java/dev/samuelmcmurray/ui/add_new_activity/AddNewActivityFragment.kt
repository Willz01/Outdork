package dev.samuelmcmurray.ui.add_new_activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentAddNewBinding

class AddNewActivityFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewActivityFragment()
    }

    private lateinit var binding : FragmentAddNewBinding
    private lateinit var viewModelProvider : AddNewActivityViewModel
    private lateinit var viewModel: AddNewActivityViewModel

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new, container, false)
        binding.lifecycleOwner = this

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
    }
}
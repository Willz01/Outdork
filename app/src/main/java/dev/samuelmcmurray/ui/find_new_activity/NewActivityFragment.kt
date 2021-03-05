package dev.samuelmcmurray.ui.find_new_activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dev.samuelmcmurray.R
import dev.samuelmcmurray.databinding.FragmentNewActivityBinding
import dev.samuelmcmurray.sampleactivities.Activities
import dev.samuelmcmurray.utilities.InjectorUtils


class NewActivityFragment : Fragment() {

    companion object {
        fun newInstance() = NewActivityFragment()
    }
    private lateinit var binding : FragmentNewActivityBinding
    private lateinit var viewModel : NewActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_activity, container, false)
        binding.lifecycleOwner = this

        val activities = Activities.returnActivities()
        val adapters : List<ContentExpandableAdapter> = activities.map { activity -> ContentExpandableAdapter(activity) }

        val concatAdapterConfig = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()

        val concatAdapter = ConcatAdapter(concatAdapterConfig, adapters)

        with(binding.rvActivities){
            layoutManager = LinearLayoutManager(context)
            itemAnimator = ExpandableItemAnimator()
            adapter = concatAdapter
        }

        /*binding.selectFilters.setOnClickListener {
            val filterItems = ContentExpandableAdapter(activities[0]).filterItemsReturn()
            println(filterItems)
        }*/

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.provideNewActivityViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(NewActivityViewModel::class.java)


        //viewModel.getAbout().observe(this, Observer {what ever we do})

    }

}
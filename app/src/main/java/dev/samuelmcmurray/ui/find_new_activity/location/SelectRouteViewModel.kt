package dev.samuelmcmurray.ui.find_new_activity.location

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.samuelmcmurray.data.model.Activity
import dev.samuelmcmurray.data.repository.SelectRouteRepository
import dev.samuelmcmurray.utilities.MyCallback
import kotlinx.coroutines.launch

private const val TAG = "SelectRouteViewModel"

class SelectRouteViewModel(application: Application) : AndroidViewModel(application) {
    private var selectRouteRepository: SelectRouteRepository = SelectRouteRepository()

    companion object{
        var activities = ArrayList<Activity>()
    }

    fun getActivities(): ArrayList<Activity> {
        viewModelScope.launch {
            try {
               selectRouteRepository.getActivities(object : MyCallback {
                   override fun onCallback(value: ArrayList<Activity>) {
                       Log.d(TAG, "onCallback: ${value.size}")
                       activities = value
                       Log.d(TAG, "onCallback: ${activities.size}")
                   }
               })
            } catch (e: Exception) {
                Log.d(TAG, "getActivities: $e")
            }

        }
        Log.d(TAG, "getActivities: ${activities.size}")
        return activities
    }
}
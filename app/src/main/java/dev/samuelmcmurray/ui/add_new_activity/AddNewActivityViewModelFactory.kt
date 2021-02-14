package dev.samuelmcmurray.ui.add_new_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.AddNewActivityRepository

class AddNewActivityViewModelFactory (private val addNewActivityRepository : AddNewActivityRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddNewActivityViewModel(addNewActivityRepository) as T
    }
}
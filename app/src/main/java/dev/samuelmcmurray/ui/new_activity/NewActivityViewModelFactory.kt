package dev.samuelmcmurray.ui.new_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.NewActivityRepository

class NewActivityViewModelFactory(private val newActivityRepository: NewActivityRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewActivityViewModel(newActivityRepository) as T
    }
}
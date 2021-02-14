package dev.samuelmcmurray.ui.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.HelpRepository

class HelpViewModelFactory(private val helpRepository: HelpRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HelpViewModel(helpRepository) as T
    }
}
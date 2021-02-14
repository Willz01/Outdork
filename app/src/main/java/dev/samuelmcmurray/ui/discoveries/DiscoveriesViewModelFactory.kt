package dev.samuelmcmurray.ui.discoveries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.DiscoveriesRepository

class DiscoveriesViewModelFactory(private val discoveriesRepository: DiscoveriesRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DiscoveriesViewModel(discoveriesRepository) as T
    }
}
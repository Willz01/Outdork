package dev.samuelmcmurray.ui.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.AboutRepository

class AboutViewModelFactory(private val aboutRepository: AboutRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AboutViewModel(aboutRepository) as T
    }
}
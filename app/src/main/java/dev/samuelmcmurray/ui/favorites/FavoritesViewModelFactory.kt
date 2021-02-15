package dev.samuelmcmurray.ui.favorites

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.FavoritesRepository
import dev.samuelmcmurray.ui.about.AboutViewModel

class FavoritesViewModelFactory(private val favoritesRepository: FavoritesRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoritesViewModel(favoritesRepository) as T
    }
}
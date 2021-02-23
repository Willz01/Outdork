package dev.samuelmcmurray.ui.favourite

import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.FavouritesRepository

class FavouriteViewModelFactory(private val favouritesRepository: FavouritesRepository) : ViewModelProvider.NewInstanceFactory() {

   /* @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavouriteViewModel(bookmarksRepository) as T
    }*/
}
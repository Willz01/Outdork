package dev.samuelmcmurray.ui.favorite

import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.FavoritesRepository

class FavoriteViewModelFactory(private val favoritesRepository: FavoritesRepository) : ViewModelProvider.NewInstanceFactory() {

   /* @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoriteViewModel(bookmarksRepository) as T
    }*/
}
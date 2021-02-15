package dev.samuelmcmurray.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.BookmarksRepository

class BookmarksViewModelFactory(private val bookmarksRepository: BookmarksRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookmarksViewModel(bookmarksRepository) as T
    }
}
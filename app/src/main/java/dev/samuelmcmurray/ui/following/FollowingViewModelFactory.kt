package dev.samuelmcmurray.ui.following

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.FollowingRepository

class FollowingViewModelFactory(private val followingRepository: FollowingRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowingViewModel(followingRepository) as T
    }
}
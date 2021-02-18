package dev.samuelmcmurray.ui.following.feeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.FollowingFeedsRepository

class FollowingFeedsViewModelFactory(private val followingFeedsRepository: FollowingFeedsRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowingFeedsViewModel(followingFeedsRepository) as T
    }
}
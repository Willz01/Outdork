package dev.samuelmcmurray.ui.following.followinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.FollowingFeedsRepository
import dev.samuelmcmurray.data.repository.FollowingListRepository
import dev.samuelmcmurray.ui.following.feeds.FollowingFeedsViewModel

class FollowingListViewModelFactory(private val followingListRepository: FollowingListRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FollowingListViewModel(followingListRepository) as T
    }
}
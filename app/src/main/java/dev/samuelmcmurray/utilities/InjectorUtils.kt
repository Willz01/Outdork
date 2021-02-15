package dev.samuelmcmurray.utilities

import dev.samuelmcmurray.data.repository.*
import dev.samuelmcmurray.ui.about.AboutViewModelFactory
import dev.samuelmcmurray.ui.add_new_activity.AddNewActivityViewModelFactory
import dev.samuelmcmurray.ui.bookmarks.BookmarksViewModelFactory
import dev.samuelmcmurray.ui.discoveries.DiscoveriesViewModel
import dev.samuelmcmurray.ui.discoveries.DiscoveriesViewModelFactory
import dev.samuelmcmurray.ui.favorites.FavoritesViewModelFactory
import dev.samuelmcmurray.ui.following.FollowingViewModelFactory
import dev.samuelmcmurray.ui.help.HelpViewModelFactory
import dev.samuelmcmurray.ui.new_activity.NewActivityViewModelFactory
import dev.samuelmcmurray.ui.report.ReportViewModelFactory

object InjectorUtils {

    fun provideAboutViewModelFactory() : AboutViewModelFactory {
        val aboutRepository = AboutRepository.getInstance("database.getInstance().AboutDAO")
        return AboutViewModelFactory(aboutRepository)
    }

    fun provideAddNewActivityViewModelFactory() : AddNewActivityViewModelFactory {
        val newActivityRepository = AddNewActivityRepository.getInstance("database.getInstance().AddNewActivityDAO")
        return AddNewActivityViewModelFactory(newActivityRepository)
    }

    fun provideBookmarksViewModelFactory() : BookmarksViewModelFactory {
        val bookmarksRepository = BookmarksRepository.getInstance("database.getInstance().BookmarksDAO")
        return BookmarksViewModelFactory(bookmarksRepository)
    }

    fun provideFavoritesViewModelFactory() : FavoritesViewModelFactory {
        val favoritesRepository = FavoritesRepository.getInstance("database.getInstance().FavoritesDAO")
        return FavoritesViewModelFactory(favoritesRepository)
    }

    fun provideFollowingViewModelFactory() : FollowingViewModelFactory {
        val followingRepository = FollowingRepository.getInstance("database.getInstance().FollowingDAO")
        return FollowingViewModelFactory(followingRepository)
    }

    fun provideHelpViewModelFactory() : HelpViewModelFactory {
        val helpRepository = HelpRepository.getInstance("database.getInstance().HelpDAO")
        return HelpViewModelFactory(helpRepository)
    }

    fun provideNewActivityViewModelFactory() : NewActivityViewModelFactory {
        val newActivityRepository = NewActivityRepository.getInstance("database.getInstance().NewActivityDAO")
        return NewActivityViewModelFactory(newActivityRepository)
    }

    fun provideReportViewModelFactory() : ReportViewModelFactory {
        val reportRepository = ReportRepository.getInstance("database.getInstance().ReportDAO")
        return ReportViewModelFactory(reportRepository)
    }

    fun provideDiscoveriesViewModelFactory() : DiscoveriesViewModelFactory {
        val discoveriesRepository = DiscoveriesRepository.getInstance("database.getInstance().DiscoveriesDAO")
        return DiscoveriesViewModelFactory(discoveriesRepository)
    }
}
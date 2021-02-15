package dev.samuelmcmurray.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.samuelmcmurray.data.repository.ReportRepository


class ReportViewModelFactory(private val reportRepository: ReportRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReportViewModel(reportRepository) as T
    }
}
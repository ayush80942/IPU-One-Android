package com.ipu.ipuoneapp.features.notices

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipu.ipuoneapp.core.network.ApiClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NoticesViewModel(private val context: Context) : ViewModel() {

    private val api = ApiClient.provideApi(context)

    var state by mutableStateOf(NoticesState())
        private set

    private var searchJob: Job? = null

    init {
        fetchNotices()
    }

    fun onCategorySelected(category: String) {
        state = state.copy(selectedCategory = category)
        fetchNotices()
    }

    fun onSearchQueryChanged(query: String) {
        state = state.copy(searchQuery = query)
        // Debounce search by 400ms to avoid hammering the API
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            fetchNotices()
        }
    }

    fun fetchNotices() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                val category = state.selectedCategory.takeIf { it != "ALL" }
                val search = state.searchQuery.trim().takeIf { it.isNotEmpty() }
                val page = api.getNotices(category = category, search = search)
                state = state.copy(isLoading = false, notices = page.content)
            } catch (e: Exception) {
                e.printStackTrace()
                state = state.copy(isLoading = false, error = e.message)
            }
        }
    }
}

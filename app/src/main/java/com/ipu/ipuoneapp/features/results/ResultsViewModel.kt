package com.ipu.ipuoneapp.features.results

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipu.ipuoneapp.core.network.ApiClient
import kotlinx.coroutines.launch

class ResultsViewModel(
    private val context: Context
) : ViewModel() {

    private val api = ApiClient.provideApi(context)

    var state by mutableStateOf(ResultsState())
        private set

    fun fetchResults() {

        viewModelScope.launch {

            state = state.copy(isLoading = true)

            try {
                val response = api.getResultDashboard()
                println("RESPONSE = $response")
                state = state.copy(
                    isLoading = false,
                    data = response
                )

            } catch (e: Exception) {
                e.printStackTrace()
                state = state.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
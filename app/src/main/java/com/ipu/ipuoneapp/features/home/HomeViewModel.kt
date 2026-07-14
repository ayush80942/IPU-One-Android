package com.ipu.ipuoneapp.features.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.ipu.ipuoneapp.core.network.ApiClient
import kotlinx.coroutines.launch

class HomeViewModel(
    private val context: Context
) : ViewModel() {

    private val api = ApiClient.provideApi(context)

    var state by mutableStateOf(HomeState())
        private set

    fun fetchStatus(onNavigate: (String) -> Unit) {

        viewModelScope.launch {

            try {
                val res = api.getAuthStatus()

                if (!res.authenticated) {
                    onNavigate("login")
                    return@launch
                }

                if (!res.studentLinked) {
                    onNavigate("import")
                    return@launch
                }

                state = state.copy(
                    isLoading = false,
                    data = res
                )

            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
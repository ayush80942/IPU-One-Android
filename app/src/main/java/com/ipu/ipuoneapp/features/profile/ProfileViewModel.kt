package com.ipu.ipuoneapp.features.profile

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipu.ipuoneapp.core.network.ApiClient
import com.ipu.ipuoneapp.core.utils.TokenManager
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val context: Context
) : ViewModel() {

    private val api = ApiClient.provideApi(context)
    private val tokenManager = TokenManager(context)

    var state by mutableStateOf(ProfileState())
        private set

    fun fetchProfile() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val profile = api.getStudentProfile()
                state = state.copy(
                    isLoading = false,
                    data = profile,
                    error = null
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

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
        }
    }
}

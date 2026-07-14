package com.ipu.ipuoneapp.features.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipu.ipuoneapp.core.network.ApiClient
import com.ipu.ipuoneapp.core.utils.TokenManager
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {

    private val api = ApiClient.provideApi(context)
    private val tokenManager = TokenManager(context)

    var state by mutableStateOf(AuthState())
        private set

    fun sendOtp(
        email: String,
        onSuccess: () -> Unit = {},
        isResend: Boolean = false
    ) {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = !isResend)

                api.sendOtp(email)

                state = state.copy(isLoading = false)
                onSuccess()

            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                val response = api.verifyOtp(email, otp)

                val token = response.token

                // 🔥 SAVE TOKEN
                tokenManager.saveToken(token)

                state = state.copy(
                    isLoading = false,
                    token = token
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
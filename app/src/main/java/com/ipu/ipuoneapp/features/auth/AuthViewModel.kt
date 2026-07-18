package com.ipu.ipuoneapp.features.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.ipu.ipuoneapp.core.config.AppConfig
import com.ipu.ipuoneapp.core.network.ApiClient
import com.ipu.ipuoneapp.core.utils.TokenManager
import com.ipu.ipuoneapp.data.model.auth.GoogleLoginRequest
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

    fun signInWithGoogle(activityContext: Context) {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true, error = null)

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(AppConfig.GOOGLE_WEB_CLIENT_ID)
                    .build()

                val credentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = CredentialManager.create(activityContext)
                    .getCredential(activityContext, credentialRequest)

                val credential = result.credential
                check(
                    credential is CustomCredential &&
                        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) { "Unexpected credential type from Credential Manager" }

                val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken

                val response = api.loginWithGoogle(GoogleLoginRequest(idToken))
                val token = response.token

                tokenManager.saveToken(token)

                state = state.copy(isLoading = false, token = token)

            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
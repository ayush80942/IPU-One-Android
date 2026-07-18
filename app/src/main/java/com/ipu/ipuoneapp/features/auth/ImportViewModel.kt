package com.ipu.ipuoneapp.features.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ipu.ipuoneapp.core.network.ApiClient
import com.ipu.ipuoneapp.core.network.ApiService
import com.ipu.ipuoneapp.data.model.auth.ImportRequest
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

private const val PORTAL_UNREACHABLE_MESSAGE =
    "We couldn't reach the university result portal. Please try again later, or contact the Exam Branch / Student Cell if this continues."

class ImportViewModel(
    private val context: Context
) : ViewModel() {

    private val api = ApiClient.provideApi(context)

    var captchaImage by mutableStateOf<String?>(null)
    var sessionId by mutableStateOf<String?>(null)

    // Captcha loading/error state is kept separate from the import/login state below —
    // a background captcha refresh should never make the Import button look like it's submitting.
    var captchaLoading by mutableStateOf(false)
    var captchaError by mutableStateOf<String?>(null)

    var loading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    fun fetchCaptcha() {
        viewModelScope.launch {
            captchaLoading = true
            captchaError = null
            try {
                val res = api.getCaptcha()
                if (res.success) {
                    captchaImage = res.captchaImage
                    sessionId = res.sessionId
                } else {
                    captchaImage = null
                    sessionId = null
                    captchaError = res.message ?: PORTAL_UNREACHABLE_MESSAGE
                }
            } catch (e: IOException) {
                // Network-level failure (timeout, unreachable, DNS, etc.) — not the user's fault.
                captchaImage = null
                sessionId = null
                captchaError = PORTAL_UNREACHABLE_MESSAGE
            } catch (e: Exception) {
                captchaImage = null
                sessionId = null
                captchaError = PORTAL_UNREACHABLE_MESSAGE
            }
            captchaLoading = false
        }
    }

    fun import(
        username: String,
        password: String,
        captcha: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            loading = true
            try {
                val currentSession = sessionId
                if (currentSession == null) {
                    error = "Captcha not loaded. Please wait for it to load or tap retry."
                    return@launch
                }

                val res = api.importResult(
                    ImportRequest(
                        username,
                        password,
                        captcha,
                        currentSession
                    )
                )

                if (res.success) {
                    onSuccess()
                } else {
                    error = res.message
                    fetchCaptcha() // 🔥 auto refresh
                }

            } catch (e: HttpException) {
                try {
                    val errorBody = e.response()?.errorBody()?.string()
                    if (errorBody != null) {
                        val json = JSONObject(errorBody)
                        error = json.optString("message", "Failed to import")
                    } else {
                        error = "Failed to import. Please try again."
                    }
                } catch (parseException: Exception) {
                    error = "Failed to import. Please try again."
                }
                fetchCaptcha() // auto refresh captcha on error
            } catch (e: IOException) {
                // Network-level failure talking to our own backend (not the university portal
                // login attempt itself, which already reports its own unreachable-portal message).
                error = PORTAL_UNREACHABLE_MESSAGE
            } catch (e: Exception) {
                error = e.message ?: "An unexpected error occurred"
            } finally {
                loading = false
            }
        }
    }
}

class ImportViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImportViewModel(context) as T
    }
}

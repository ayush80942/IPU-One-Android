package com.ipu.ipuoneapp.core.network

import android.content.Context
import com.ipu.ipuoneapp.core.utils.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import kotlinx.coroutines.flow.firstOrNull

class AuthInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val tokenManager = TokenManager(context)

        // 🔥 Get token synchronously (important)
        val token = runBlocking {
            tokenManager.token
                .firstOrNull()
        }

        val request = chain.request().newBuilder()

        // 🔥 Attach token if exists
        token?.let {
            request.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(request.build())
    }
}
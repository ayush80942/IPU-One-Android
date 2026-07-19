package com.ipu.ipuoneapp.core.network

import android.content.Context
import coil.ImageLoader
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.ipu.ipuoneapp.core.config.AppConfig

object ApiClient {

    private const val BASE_URL = AppConfig.BASE_URL

    fun provideApi(context: Context): ApiService {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    /**
     * Document/profile files are served from authenticated endpoints (e.g. GET
     * /api/documents/{id}/file), not embedded as base64 in JSON — so images loaded via Coil
     * need the same bearer token attached as Retrofit calls.
     */
    fun provideImageLoader(context: Context): ImageLoader {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        return ImageLoader.Builder(context)
            .okHttpClient(client)
            .build()
    }

    fun resolveUrl(path: String): String {
        return if (path.startsWith("http")) path else BASE_URL.trimEnd('/') + "/" + path.trimStart('/')
    }
}
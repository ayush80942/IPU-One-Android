package com.ipu.ipuoneapp.features.auth

data class AuthState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val error: String? = null
)

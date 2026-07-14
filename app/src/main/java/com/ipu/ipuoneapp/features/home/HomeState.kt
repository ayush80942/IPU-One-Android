package com.ipu.ipuoneapp.features.home

import com.ipu.ipuoneapp.data.model.auth.AuthStatusResponse

data class HomeState(
    val isLoading: Boolean = true,
    val data: AuthStatusResponse? = null,
    val error: String? = null
)
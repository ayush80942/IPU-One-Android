package com.ipu.ipuoneapp.data.model.auth

data class ImportResponse(
    val success: Boolean,
    val message: String,
    val sessionId: String?
)

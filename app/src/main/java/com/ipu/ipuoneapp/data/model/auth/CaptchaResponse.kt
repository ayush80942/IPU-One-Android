package com.ipu.ipuoneapp.data.model.auth

data class CaptchaResponse(
    val success: Boolean,
    val captchaImage: String,
    val sessionId: String
)
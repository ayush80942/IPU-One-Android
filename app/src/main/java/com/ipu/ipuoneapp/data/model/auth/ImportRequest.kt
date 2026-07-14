package com.ipu.ipuoneapp.data.model.auth

data class ImportRequest(
    val username: String,
    val password: String,
    val captcha: String,
    val sessionId: String
)

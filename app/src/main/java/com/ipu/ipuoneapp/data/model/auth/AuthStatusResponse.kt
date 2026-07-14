package com.ipu.ipuoneapp.data.model.auth

data class AuthStatusResponse(
    val authenticated: Boolean,
    val studentLinked: Boolean,
    val enrollmentNo: String?,

    val name: String?,
    val profileImage: String?,
    val programName: String?,
    val instituteName: String?,
    val batchYear: Int?
)
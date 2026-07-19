package com.ipu.ipuoneapp.data.model.document

data class DocumentRequestDto(
    val documentType: String,
    val semester: String? = null,
    val examType: String? = null,
    val nocDuration: String? = null,
    val imageBase64: String
)

data class DocumentResponseDto(
    val id: Long,
    val enrollmentNo: String,
    val documentType: String,
    val semester: String?,
    val examType: String?,
    val nocDuration: String?,
    val fileUrl: String,
    val submittedAt: String,
    val updatedAt: String
)

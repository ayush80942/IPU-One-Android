package com.ipu.ipuoneapp.data.model.notice

data class NoticeResponseDto(
    val id: Long,
    val category: String,
    val badge: String?,
    val title: String,
    val description: String,
    val actionText: String,
    val isPdf: Boolean,
    val actionUrl: String,
    val targetProgramCodes: String?,
    val targetInstituteCodes: String?,
    val targetBatchYears: String?,
    val targetAdmissionYears: String?,
    val date: String
)

package com.ipu.ipuoneapp.data.model.student

data class StudentProfileDTO(
    val enrollmentNo: String,
    val name: String,
    val batchYear: Int?,
    val admissionYear: Int?,
    val programCode: String?,
    val programName: String?,
    val instituteCode: String?,
    val instituteName: String?,
    val gender: String?,
    val fatherName: String?,
    val motherName: String?,
    val contactNumber: String?,
    val email: String?,
    val profileImage: String?
)

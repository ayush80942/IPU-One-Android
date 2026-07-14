package com.ipu.ipuoneapp.features.profile

import com.ipu.ipuoneapp.data.model.student.StudentProfileDTO

data class ProfileState(
    val isLoading: Boolean = true,
    val data: StudentProfileDTO? = null,
    val error: String? = null
)

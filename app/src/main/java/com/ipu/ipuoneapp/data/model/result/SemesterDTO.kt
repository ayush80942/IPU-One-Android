package com.ipu.ipuoneapp.data.model.result

data class SemesterDTO(
    val semester: Int,
    val sgpa: Double,
    val percentage: Double,
    val credits: Int,
    val backlogs: Int,
    val totalMarks: Int,
    val obtainedMarks: Int,
    val declaredDate: String
)

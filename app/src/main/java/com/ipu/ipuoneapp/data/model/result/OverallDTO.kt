package com.ipu.ipuoneapp.data.model.result

data class OverallDTO(
    val cgpa: Double,
    val percentage: Double,
    val totalCredits: Int,
    val backlogs: Int,
    val totalMarks: Int,
    val obtainedMarks: Int
)

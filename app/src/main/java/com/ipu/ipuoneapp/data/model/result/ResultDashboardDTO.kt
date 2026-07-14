package com.ipu.ipuoneapp.data.model.result

data class ResultDashboardDTO(
    val overall: OverallDTO,
    val semesters: List<SemesterDTO>,
    val subjects: Map<Int, List<SubjectDTO>>,
    val trend: List<Double>,
    val lastUpdated: String
)

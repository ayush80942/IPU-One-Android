package com.ipu.ipuoneapp.data.model.result

data class SubjectDTO(
    val paperCode: String,
    val subjectName: String,
    val internalMarks: Int?,
    val externalMarks: Int?,
    val totalMarks: Int?,
    val credits: Int?,
    val gradePoint: Int?,
    val grade: String?,
    val status: String?,
    val graceApplied: Boolean?
)

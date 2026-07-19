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

/**
 * Human-readable label for a document card. Plain "Marksheet" is indistinguishable across
 * semesters, so marksheets get their semester appended — e.g. "Marksheet (Sem 1)".
 */
fun DocumentResponseDto.displayLabel(): String {
    val baseName = documentType
        .replace("_", " ")
        .lowercase()
        .split(" ")
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

    return if (documentType == "MARKSHEET" && !semester.isNullOrBlank()) {
        "$baseName (Sem $semester)"
    } else {
        baseName
    }
}

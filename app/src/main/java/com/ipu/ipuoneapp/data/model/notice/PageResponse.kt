package com.ipu.ipuoneapp.data.model.notice

/**
 * Generic Spring Boot Page<T> response wrapper.
 * Maps the `content`, `totalPages`, and `totalElements` fields from a paginated API response.
 */
data class PageResponse<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val number: Int,     // current page (0-indexed)
    val last: Boolean    // true if this is the final page
)

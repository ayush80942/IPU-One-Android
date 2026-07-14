package com.ipu.ipuoneapp.features.notices

import com.ipu.ipuoneapp.data.model.notice.NoticeResponseDto

data class NoticesState(
    val isLoading: Boolean = true,
    val notices: List<NoticeResponseDto> = emptyList(),
    val error: String? = null,
    val selectedCategory: String = "ALL",
    val searchQuery: String = ""
)

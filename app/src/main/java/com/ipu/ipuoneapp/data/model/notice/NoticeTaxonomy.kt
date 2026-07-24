package com.ipu.ipuoneapp.data.model.notice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.ipu.ipuoneapp.core.ui.theme.BadgeNew
import com.ipu.ipuoneapp.core.ui.theme.BadgeUrgent
import com.ipu.ipuoneapp.core.ui.theme.CategoryCircular
import com.ipu.ipuoneapp.core.ui.theme.CategoryExam
import com.ipu.ipuoneapp.core.ui.theme.CategoryInternship
import com.ipu.ipuoneapp.core.ui.theme.CategoryPlacement
import com.ipu.ipuoneapp.core.ui.theme.CategoryScholarship

/**
 * Single source of truth for notice classification: category is the primary,
 * always-shown dimension (color + icon + label); badge (URGENT/NEW) is a
 * secondary, optional accent and must never be substituted for category.
 */

// (value, display label) pairs for the filter chip row.
val NOTICE_CATEGORY_FILTERS: List<Pair<String, String>> = listOf(
    "ALL" to "All",
    "EXAM" to "Exam",
    "SCHOLARSHIP" to "Scholarship",
    "INTERNSHIP" to "Internship",
    "CIRCULAR" to "Circular",
    "PLACEMENT" to "Placement",
)

fun String.categoryLabel(): String = when (uppercase()) {
    "EXAM" -> "Exam"
    "SCHOLARSHIP" -> "Scholarship"
    "INTERNSHIP" -> "Internship"
    "CIRCULAR" -> "Circular"
    "PLACEMENT" -> "Placement"
    else -> replaceFirstChar { it.uppercase() }
}

fun String.categoryColor(): Color = when (uppercase()) {
    "EXAM" -> CategoryExam
    "SCHOLARSHIP" -> CategoryScholarship
    "INTERNSHIP" -> CategoryInternship
    "CIRCULAR" -> CategoryCircular
    "PLACEMENT" -> CategoryPlacement
    else -> CategoryExam
}

fun String.categoryIcon(): ImageVector = when (uppercase()) {
    "EXAM" -> Icons.Default.School
    "SCHOLARSHIP" -> Icons.Default.CardGiftcard
    "INTERNSHIP" -> Icons.Default.Work
    "CIRCULAR" -> Icons.Default.Campaign
    "PLACEMENT" -> Icons.Default.BusinessCenter
    else -> Icons.Default.Campaign
}

fun NoticeResponseDto.categoryLabel(): String = category.categoryLabel()
fun NoticeResponseDto.categoryColor(): Color = category.categoryColor()
fun NoticeResponseDto.categoryIcon(): ImageVector = category.categoryIcon()

// Badge is optional — these return null rather than ever falling back to category.
fun String?.badgeLabel(): String? = when (this?.uppercase()) {
    "URGENT" -> "Urgent"
    "NEW" -> "New"
    else -> null
}

fun String?.badgeColor(): Color? = when (this?.uppercase()) {
    "URGENT" -> BadgeUrgent
    "NEW" -> BadgeNew
    else -> null
}

fun NoticeResponseDto.badgeLabel(): String? = badge.badgeLabel()
fun NoticeResponseDto.badgeColor(): Color? = badge.badgeColor()

package com.ipu.ipuoneapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipu.ipuoneapp.data.model.notice.badgeColor
import com.ipu.ipuoneapp.data.model.notice.badgeLabel
import com.ipu.ipuoneapp.data.model.notice.categoryColor
import com.ipu.ipuoneapp.data.model.notice.categoryIcon
import com.ipu.ipuoneapp.data.model.notice.categoryLabel

/**
 * The always-shown, primary classification pill: icon + label, tinted per
 * category. Shared by NoticesScreen's NoticeCard and the home dashboard's
 * HomeNoticeCard so both surfaces render notices identically.
 */
@Composable
fun NoticeCategoryChip(category: String, modifier: Modifier = Modifier) {
    val color = category.categoryColor()
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = category.categoryIcon(),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = category.categoryLabel(),
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * The secondary, optional urgency/recency accent (URGENT/NEW). Renders
 * nothing when there's no badge — callers must never substitute the
 * category for a missing badge.
 */
@Composable
fun NoticeBadgePill(badge: String?, modifier: Modifier = Modifier) {
    val color = badge.badgeColor() ?: return
    val label = badge.badgeLabel() ?: return
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

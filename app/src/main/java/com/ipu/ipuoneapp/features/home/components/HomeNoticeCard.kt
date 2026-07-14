package com.ipu.ipuoneapp.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HomeNoticeCard(
    badge: String,
    date: String,
    title: String,
    description: String,
    bottomLabel: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val isUrgent = badge.uppercase() == "URGENT"
    val badgeColor = if (isUrgent) colors.primary else Color(0xFF2980B9)
    val badgeTextColor = Color.White
    val bottomIconColor = if (isUrgent) colors.primary else Color(0xFF2980B9)
    val bottomIcon = if (isUrgent) Icons.Default.EventNote else Icons.Default.BusinessCenter

    Card(
        modifier = modifier
            .width(260.dp)
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50)) // Pill shape
                            .background(if (isUrgent) colors.errorContainer else colors.primaryContainer)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badge.uppercase(),
                            color = if (isUrgent) colors.onErrorContainer else colors.primary,
                            style = typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = date,
                        style = typography.labelSmall,
                        color = colors.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = title,
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    style = typography.bodySmall.copy(
                        color = colors.onSurfaceVariant
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = bottomIcon,
                    contentDescription = null,
                    tint = bottomIconColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = bottomLabel,
                    style = typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = bottomIconColor
                    )
                )
            }
        }
    }
}

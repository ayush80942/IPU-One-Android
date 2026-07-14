package com.ipu.ipuoneapp.features.home.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun HomeHeader(name: String = "Ayush") {

    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // ✅ Format Name
    val formattedName = name
        .trim()
        .split(" ")
        .firstOrNull()
        ?.lowercase()
        ?.replaceFirstChar { it.uppercase() }
        ?: ""

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Welcome back, $formattedName",
                style = typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        }

        Box {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notifications"
                )
            }

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-6).dp, y = 6.dp)
                    .background(
                        color = colors.primary,
                        shape = CircleShape
                    )
            )
        }
    }
}
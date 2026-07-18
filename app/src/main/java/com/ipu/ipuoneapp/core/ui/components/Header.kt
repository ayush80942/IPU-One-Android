package com.ipu.ipuoneapp.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Shared onboarding header ("IPU One" / "STUDENT PORTAL").
 * Pass [onBack] to overlay a back button (e.g. the refresh-import flow); omit it for a plain centered header.
 */
@Composable
fun Header(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null
) {
    Box(modifier = modifier.fillMaxWidth()) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "IPU One",
                style = typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                "STUDENT PORTAL",
                style = typography.labelMedium.copy(
                    letterSpacing = 2.sp
                )
            )
        }
    }
}
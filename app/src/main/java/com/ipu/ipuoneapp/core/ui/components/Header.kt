package com.ipu.ipuoneapp.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxWidth(),
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
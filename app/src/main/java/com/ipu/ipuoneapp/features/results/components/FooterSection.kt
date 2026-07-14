package com.ipu.ipuoneapp.features.results.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ipu.ipuoneapp.core.ui.components.PrimaryButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toFormattedDate(): String {
    return try {
        val date = LocalDate.parse(this) // ✅ directly parse
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
        date.format(formatter)
    } catch (e: Exception) {
        this
    }
}
@Composable
fun FooterSection(
    lastUpdated: String,
    onRefresh: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🕒 Last Updated Text
        Text(
            text = "Last updated : ${lastUpdated.toFormattedDate()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔴 Primary Button (Premium)
        PrimaryButton(
            text = "↻ Refresh Results",
            onClick = onRefresh
        )
    }
}
package com.ipu.ipuoneapp.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ipu.ipuoneapp.core.network.ApiClient
import com.ipu.ipuoneapp.core.ui.components.DocumentViewerDialog
import com.ipu.ipuoneapp.data.model.document.DocumentResponseDto
import com.ipu.ipuoneapp.data.model.document.displayLabel
import com.ipu.ipuoneapp.features.services.collect.CollectViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecentDocumentsSection(
    onViewAll: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { CollectViewModel(context) }
    val imageLoader = remember { ApiClient.provideImageLoader(context) }

    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // State for viewing/downloading a document
    var viewingDocument by remember { mutableStateOf<DocumentResponseDto?>(null) }

    Column {
        // Header row — same style as HomeResultStatsSection & LatestNoticesSection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Documents",
                style = typography.headlineSmall
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onViewAll() }
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View All",
                    style = typography.labelLarge.copy(
                        color = colors.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        when {
            viewModel.isLoadingDocuments -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp),
                        color = colors.primary
                    )
                }
            }

            viewModel.myDocuments.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No documents collected yet",
                        style = typography.bodySmall.copy(color = colors.outline)
                    )
                }
            }

            else -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    viewModel.myDocuments.take(3).forEach { doc ->
                        HomeDocumentCard(
                            document = doc,
                            onViewDocument = { viewingDocument = doc }
                        )
                    }
                }
            }
        }
    }

    // Document Viewing/Download Dialog
    viewingDocument?.let { doc ->
        DocumentViewerDialog(
            document = doc,
            imageLoader = imageLoader,
            onDismiss = { viewingDocument = null }
        )
    }
}

@Composable
private fun HomeDocumentCard(
    document: DocumentResponseDto,
    onViewDocument: () -> Unit
) {
    val formattedDate = remember(document.submittedAt) {
        try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
            val date = parser.parse(document.submittedAt)
            if (date != null) formatter.format(date) else null
        } catch (_: Exception) {
            null
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Icon Box
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFF6EFEF), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFF5E3E3), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Document",
                    tint = Color(0xFF7A0000),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Middle Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = document.displayLabel(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                if (formattedDate != null) {
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onViewDocument() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "View",
                        tint = Color(0xFF8B0000),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "View Document",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF8B0000),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

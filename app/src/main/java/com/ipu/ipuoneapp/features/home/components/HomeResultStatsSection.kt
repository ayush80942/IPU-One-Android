package com.ipu.ipuoneapp.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipu.ipuoneapp.features.results.ResultsViewModel

@Composable
fun HomeResultStatsSection(
    onNavigateToResults: () -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember { ResultsViewModel(context) }
    
    LaunchedEffect(Unit) {
        viewModel.fetchResults()
    }

    val state = viewModel.state
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(modifier = Modifier.fillMaxWidth()) {
        // Aligned Heading Style
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Results",
                style = typography.headlineSmall
            )
            
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onNavigateToResults() }
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View Details",
                    style = typography.labelLarge.copy(
                        color = colors.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                }
            }
            state.data != null -> {
                val overall = state.data.overall
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatItem(
                            title = "CGPA",
                            value = "%.2f".format(overall.cgpa),
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(colors.onSurface.copy(alpha = 0.1f))
                        )
                        StatItem(
                            title = "Credits",
                            value = "${overall.totalCredits}",
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(colors.onSurface.copy(alpha = 0.1f))
                        )
                        StatItem(
                            title = "Backlogs",
                            value = "${overall.backlogs}",
                            valueColor = if (overall.backlogs > 0) colors.error else colors.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Results not available",
                        style = typography.bodyMedium.copy(color = colors.onSurfaceVariant)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    title: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = value,
            style = typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                color = valueColor,
                letterSpacing = (-0.5).sp
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            style = typography.labelMedium.copy(
                color = colors.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

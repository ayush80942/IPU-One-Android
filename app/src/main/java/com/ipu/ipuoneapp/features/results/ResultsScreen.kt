package com.ipu.ipuoneapp.features.results

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ipu.ipuoneapp.data.model.result.ResultDashboardDTO
import com.ipu.ipuoneapp.data.model.result.SemesterDTO
import com.ipu.ipuoneapp.data.model.result.SubjectDTO
import com.ipu.ipuoneapp.features.results.components.FooterSection
import com.ipu.ipuoneapp.features.results.components.SemesterDropdown
import com.ipu.ipuoneapp.features.results.components.SemesterHistorySection
import com.ipu.ipuoneapp.features.results.components.SummaryCard
import com.ipu.ipuoneapp.features.results.components.TrendChart

@Composable
fun ResultsScreen(
    onNavigateToImport: () -> Unit = {}
) {

    val context = LocalContext.current
    val viewModel = remember { ResultsViewModel(context) }
    val state = viewModel.state

    var selectedSemester by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchResults()
    }

    when {

        // 🔄 LOADING
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        // ❌ ERROR (IMPORTANT)
        state.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}")
            }
        }

        // ✅ DATA
        state.data != null -> {

            val data = state.data

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                SemesterDropdown(
                    semesters = data.semesters.map { it.semester },
                    selectedSemester = selectedSemester,
                    onSelect = { selectedSemester = it }
                )

                Spacer(Modifier.height(8.dp))

                if (selectedSemester == null) {

                    OverallSection(data, onSemesterClick = { selectedSemester = it })

                } else {

                    // ✅ SAFE FIND (no crash)
                    val semester = data.semesters.find {
                        it.semester == selectedSemester
                    }

                    if (semester != null) {
                        SemesterSection(
                            semester = semester,
                            subjects = data.subjects[selectedSemester] ?: emptyList()
                        )
                    } else {
                        Text("No data for selected semester")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                FooterSection(
                    lastUpdated = data.lastUpdated,
                    onRefresh = { onNavigateToImport() }
                )
            }
        }

        // ⚠️ FALLBACK (CRITICAL)
        else -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No data available")
            }
        }
    }
}

@Composable
fun OverallSection(data: ResultDashboardDTO, onSemesterClick: (Int) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SummaryCard(
                title = "CGPA",
                value = "%.2f".format(data.overall.cgpa),
                valueColor = MaterialTheme.colorScheme.secondary,
                suffix = "/ 10.0",
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Percentage",
                value = "%.2f".format(data.overall.percentage),
                valueColor = MaterialTheme.colorScheme.onBackground,
                suffix = "%",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            SummaryCard(
                title = "Total Credits",
                value = data.overall.totalCredits.toString(),
                valueColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Backlogs",
                value = data.overall.backlogs.toString(),
                valueColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
    }

    Spacer(Modifier.height(12.dp))

    TrendChart(data.trend)

    Spacer(Modifier.height(16.dp))

    SemesterHistorySection(
        semesters = data.semesters,
        onSemesterClick = onSemesterClick
    )
}

@Composable
fun SemesterSection(
    semester: SemesterDTO,
    subjects: List<SubjectDTO>
) {
    var showBreakdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            SummaryCard(
                title = "SGPA",
                value = "%.2f".format(semester.sgpa),
                valueColor = MaterialTheme.colorScheme.secondary,
                suffix = "/ 10.0",
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Percentage",
                value = "%.2f".format(semester.percentage),
                valueColor = MaterialTheme.colorScheme.onBackground,
                suffix = "%",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            SummaryCard(
                title = "Credits",
                value = semester.credits.toString(),
                valueColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Backlogs",
                value = semester.backlogs.toString(),
                valueColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }
    }

    Spacer(Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Subjects", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        
        // Premium Segmented Control
        Row(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (!showBreakdown) MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable { showBreakdown = false }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    "Total",
                    style = MaterialTheme.typography.labelMedium, 
                    fontWeight = if(!showBreakdown) FontWeight.Bold else FontWeight.Normal,
                    color = if(!showBreakdown) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (showBreakdown) MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable { showBreakdown = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    "Breakdown",
                    style = MaterialTheme.typography.labelMedium, 
                    fontWeight = if(showBreakdown) FontWeight.Bold else FontWeight.Normal,
                    color = if(showBreakdown) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Table Header
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "SUBJECT (CREDITS)",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.weight(1f).padding(end = 16.dp)
        )
        Text(
            if (showBreakdown) "INT. | EXT." else "MARKS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outline
        )
    }
    
    Divider(color = MaterialTheme.colorScheme.outlineVariant)

    subjects.forEach {
        SubjectRow(it, showBreakdown)
        Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 0.5.dp)
    }
}

@Composable
fun SubjectRow(subject: SubjectDTO, showBreakdown: Boolean) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${subject.subjectName} (${subject.credits ?: "-"})",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f).padding(end = 16.dp)
        )

        if (showBreakdown) {
            Text(
                text = "${subject.internalMarks ?: "-"} | ${subject.externalMarks ?: "-"}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            val isFail = "F".equals(subject.grade, ignoreCase = true)
            Text(
                text = "${subject.totalMarks ?: "-"} | ${subject.grade ?: "-"}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (isFail) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}
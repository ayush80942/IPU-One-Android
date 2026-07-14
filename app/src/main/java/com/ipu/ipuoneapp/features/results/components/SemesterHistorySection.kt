package com.ipu.ipuoneapp.features.results.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipu.ipuoneapp.data.model.result.SemesterDTO

@Composable
fun SemesterCard(
    semester: SemesterDTO,
    onClick: () -> Unit
) {
    val sgpaColor = MaterialTheme.colorScheme.primary
    val percentageColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    val titleColor = MaterialTheme.colorScheme.onSurface
    val subtitleColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Semester ${semester.semester}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = titleColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${semester.credits} Credits Earned",
                    style = MaterialTheme.typography.bodyMedium,
                    color = subtitleColor
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${"%.1f".format(semester.sgpa)} SGPA",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = sgpaColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${"%.1f".format(semester.percentage)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = percentageColor
                )
            }
        }
    }
}

@Composable
fun SemesterHistorySection(
    semesters: List<SemesterDTO>,
    onSemesterClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Academic History",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )

        // sortedByDescending puts the highest (latest) semester on top
        semesters.sortedByDescending { it.semester }.forEachIndexed { index, semester ->
            SemesterCard(
                semester = semester,
                onClick = { onSemesterClick(semester.semester) }
            )
        }
    }
}
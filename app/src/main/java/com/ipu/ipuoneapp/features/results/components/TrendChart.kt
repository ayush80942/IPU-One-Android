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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.component.text.VerticalPosition
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.formatter.DecimalFormatValueFormatter

@Composable
fun TrendChart(trend: List<Double>) {

    if (trend.isEmpty()) {
        Text("No trend data available")
        return
    }

    val primaryColor = MaterialTheme.colorScheme.primary

    val entries = entryModelOf(
        *trend.mapIndexed { index, value ->
            index.toFloat() to value.toFloat()
        }.toTypedArray()
    )

    val lineChart = lineChart(
        lines = listOf(
            lineSpec(
                lineColor = primaryColor,
                lineThickness = DefaultDimens.LINE_THICKNESS.dp,
                lineBackgroundShader = DynamicShaders.fromBrush(
                    Brush.verticalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.25f),
                            primaryColor.copy(alpha = 0f)
                        )
                    )
                ),
                lineCap = StrokeCap.Round,
                point = ShapeComponent(
                    shape = Shapes.pillShape,
                    color = primaryColor.toArgb(),
                ),
                pointSize = DefaultDimens.POINT_SIZE.dp,
                dataLabel = textComponent(),
                dataLabelVerticalPosition = VerticalPosition.Top,
                dataLabelValueFormatter = DecimalFormatValueFormatter(),
                dataLabelRotationDegrees = 0f,
                pointConnector = DefaultPointConnector(cubicStrength = 0.5f), // 👈 fixed
            )
        ),
        spacing = 48.dp
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "GPA Trend",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Last ${trend.size} Semester${if (trend.size != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Chart(
                chart = lineChart,
                model = entries,
                bottomAxis = rememberBottomAxis(
                    valueFormatter = { x, _ -> "SEM ${(x + 1).toInt()}" },
                    guideline = null,
                    axis = null,
                    tick = null,
                ),
                startAxis = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}
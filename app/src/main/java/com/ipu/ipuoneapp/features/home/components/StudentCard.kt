package com.ipu.ipuoneapp.features.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipu.ipuoneapp.data.model.auth.AuthStatusResponse
import java.time.LocalDate

@Composable
fun StudentCard(data: AuthStatusResponse) {

    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val imageBitmap = remember(data.profileImage) {
        decodeBase64ToBitmap(data.profileImage)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {

        Column(modifier = Modifier.padding(20.dp)) {

            // 🔹 TOP SECTION (IMAGE + NAME)
            Row(verticalAlignment = Alignment.CenterVertically) {

                // 👇 Profile Image
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    )
                } else {
                    DefaultAvatar(colors)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = data.name ?: "—",
                        style = typography.headlineSmall,
                        maxLines = 1
                    )

                    Text(
                        text = data.enrollmentNo ?: "",
                        style = typography.labelMedium.copy(
                            color = colors.primary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 INFO SECTION (2 ROW LAYOUT)

            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 👈 Institute (takes max space)
                    InfoItem(
                        title = "Institute",
                        value = data.instituteName ?: "—",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // 👉 Current Year (right aligned)
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        InfoItem(
                            title = "Current Year & Sem",
                            value = calculateYearAndSem(data.batchYear)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    InfoItem(
                        title = "Program",
                        value = data.programName ?: "—",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colorScheme

    Column(modifier = modifier.padding(end = 8.dp)) {
        Text(
            text = title,
            style = typography.labelSmall,
            color = colors.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 2
        )
    }
}

@Composable
fun DefaultAvatar(colors: ColorScheme) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(colors.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            tint = colors.onSurface.copy(alpha = 0.6f)
        )
    }
}

fun decodeBase64ToBitmap(base64: String?): ImageBitmap? {
    return try {
        if (base64.isNullOrEmpty()) return null

        val cleanBase64 = base64.substringAfter("base64,", base64)
        val bytes = android.util.Base64.decode(cleanBase64, android.util.Base64.DEFAULT)
        val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}

fun calculateYearAndSem(batchYear: Int?): String {
    if (batchYear == null) return "—"

    val now = LocalDate.now()
    val year = now.year
    val month = now.monthValue

    // Academic year adjustment (starts August)
    val academicYear = if (month < 8) year - 1 else year

    val yearOfStudy = academicYear - batchYear + 1

    if (yearOfStudy !in 1..5) return "—"

    // Semester calculation
    val isEvenSem = month in 1..6
    val semester = if (isEvenSem) {
        yearOfStudy * 2
    } else {
        yearOfStudy * 2 - 1
    }

    return "Year $yearOfStudy | Sem $semester"
}
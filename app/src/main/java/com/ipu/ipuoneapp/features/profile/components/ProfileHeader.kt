package com.ipu.ipuoneapp.features.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ProfileHeader(
    name: String,
    enrollmentNo: String,
    profileImage: String?,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val imageBitmap = remember(profileImage) {
        decodeBase64ToBitmap(profileImage)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        // 👤 Profile Image
        Box(
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        width = 3.dp,
                        color = colors.primary.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(colors.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 👤 Name
        Text(
            text = name,
            style = typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = colors.onBackground
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 🆔 Enrollment
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(colors.primary.copy(alpha = 0.08f))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = "Enrollment: $enrollmentNo",
                style = typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

private fun decodeBase64ToBitmap(base64: String?): ImageBitmap? {
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
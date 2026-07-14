package com.ipu.ipuoneapp.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipu.ipuoneapp.features.profile.components.ProfileHeader

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel = remember { ProfileViewModel(context) }
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.fetchProfile()
    }

    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colors.primary)
            }
        }
        state.error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Error Loading Profile",
                        style = typography.headlineSmall.copy(color = colors.primary, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.error ?: "Unknown error",
                        style = typography.bodyMedium.copy(color = colors.onBackground.copy(alpha = 0.7f)),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.fetchProfile() },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                    ) {
                        Text("Retry", color = colors.onPrimary)
                    }
                }
            }
        }
        state.data != null -> {
            val student = state.data

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Header
                ProfileHeader(
                    name = student.name,
                    enrollmentNo = student.enrollmentNo,
                    profileImage = student.profileImage
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card 1: Academic & University Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Academic Details",
                            style = typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = colors.primary,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        ProfileRowItem(
                            icon = Icons.Default.School,
                            label = "Program Name",
                            value = student.programName ?: "—"
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.onBackground.copy(alpha = 0.08f))

                        ProfileRowItem(
                            icon = Icons.Default.HomeWork,
                            label = "Institute Name",
                            value = student.instituteName ?: "—"
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.onBackground.copy(alpha = 0.08f))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) {
                                ProfileRowItem(
                                    icon = Icons.Default.CalendarToday,
                                    label = "Batch Year",
                                    value = student.batchYear?.toString() ?: "—"
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                ProfileRowItem(
                                    icon = Icons.Default.DateRange,
                                    label = "Admission Year",
                                    value = student.admissionYear?.toString() ?: "—"
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card 2: Personal Details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Personal Information",
                            style = typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = colors.primary,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        ProfileRowItem(
                            icon = Icons.Default.Person,
                            label = "Father's Name",
                            value = student.fatherName ?: "—"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.onBackground.copy(alpha = 0.08f))

                        ProfileRowItem(
                            icon = Icons.Default.Person,
                            label = "Mother's Name",
                            value = student.motherName ?: "—"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.onBackground.copy(alpha = 0.08f))

                        ProfileRowItem(
                            icon = Icons.Default.Info,
                            label = "Gender",
                            value = student.gender ?: "—"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.onBackground.copy(alpha = 0.08f))

                        ProfileRowItem(
                            icon = Icons.Default.Phone,
                            label = "Contact Number",
                            value = student.contactNumber ?: "—"
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.onBackground.copy(alpha = 0.08f))

                        ProfileRowItem(
                            icon = Icons.Default.Email,
                            label = "Email Address",
                            value = student.email ?: "—"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                OutlinedButton(
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF7A0000)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF7A0000)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        style = typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Attribution Footer
                val uriHandler = LocalUriHandler.current
                val linkedInUrl = "https://www.linkedin.com/in/ayush80942/"

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = linkedInIcon(),
                        contentDescription = "LinkedIn Profile",
                        tint = Color(0xFF0A66C2),
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { uriHandler.openUri(linkedInUrl) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Designed & Developed with ❤️ by\nAyush Aggarwal — AI&DS'26, USAR",
                        style = typography.labelSmall.copy(
                            color = colors.onBackground.copy(alpha = 0.38f),
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background),
                contentAlignment = Alignment.Center
            ) {
                Text("No data available")
            }
        }
    }
}

fun linkedInIcon(): ImageVector = ImageVector.Builder(
    name = "LinkedIn",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color.Black)
    ) {
        // Rounded rectangle background
        moveTo(20.447f, 20.452f)
        horizontalLineTo(16.893f)
        verticalLineTo(14.883f)
        curveTo(16.893f, 13.555f, 16.866f, 11.846f, 15.041f, 11.846f)
        curveTo(13.188f, 11.846f, 12.905f, 13.291f, 12.905f, 14.785f)
        verticalLineTo(20.452f)
        horizontalLineTo(9.351f)
        verticalLineTo(9f)
        horizontalLineTo(12.765f)
        verticalLineTo(10.561f)
        horizontalLineTo(12.811f)
        curveTo(13.288f, 9.661f, 14.448f, 8.711f, 16.181f, 8.711f)
        curveTo(19.782f, 8.711f, 20.448f, 11.081f, 20.448f, 14.166f)
        verticalLineTo(20.452f)
        close()
        moveTo(5.337f, 7.433f)
        curveTo(4.193f, 7.433f, 3.274f, 6.507f, 3.274f, 5.368f)
        curveTo(3.274f, 4.23f, 4.194f, 3.305f, 5.337f, 3.305f)
        curveTo(6.477f, 3.305f, 7.401f, 4.23f, 7.401f, 5.368f)
        curveTo(7.401f, 6.507f, 6.476f, 7.433f, 5.337f, 7.433f)
        close()
        moveTo(7.119f, 20.452f)
        horizontalLineTo(3.555f)
        verticalLineTo(9f)
        horizontalLineTo(7.119f)
        verticalLineTo(20.452f)
        close()
        moveTo(22.225f, 0f)
        horizontalLineTo(1.771f)
        curveTo(0.792f, 0f, 0f, 0.774f, 0f, 1.729f)
        verticalLineTo(22.271f)
        curveTo(0f, 23.227f, 0.792f, 24f, 1.771f, 24f)
        horizontalLineTo(22.222f)
        curveTo(23.203f, 24f, 24f, 23.227f, 24f, 22.271f)
        verticalLineTo(1.729f)
        curveTo(24f, 0.774f, 23.203f, 0f, 22.222f, 0f)
        close()
    }
}.build()

@Composable
fun ProfileRowItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colors.primary.copy(alpha = 0.06f), shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = colors.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = typography.labelMedium.copy(
                    color = colors.onBackground.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = typography.bodyMedium.copy(
                    color = colors.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}
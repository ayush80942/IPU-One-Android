package com.ipu.ipuoneapp.features.auth.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipu.ipuoneapp.core.ui.components.AppTextField
import com.ipu.ipuoneapp.core.ui.components.PrimaryButton
import com.ipu.ipuoneapp.core.ui.components.Header
import com.ipu.ipuoneapp.core.ui.components.Footer
import com.ipu.ipuoneapp.features.auth.ImportViewModel
import com.ipu.ipuoneapp.features.auth.ImportViewModelFactory

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.platform.LocalContext

@Composable
fun ImportScreen(
    isRefresh: Boolean = false,
    prefilledEnroll: String = "",
    onImportSuccess: () -> Unit,
    onBack: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val viewModel: ImportViewModel = viewModel(
        factory = ImportViewModelFactory(context)
    )

    var enroll by remember { mutableStateOf(prefilledEnroll) }
    var password by remember { mutableStateOf("") }
    var captcha by remember { mutableStateOf("") }

    val base64String = viewModel.captchaImage
        ?.substringAfter("base64,")

    LaunchedEffect(Unit) {
        viewModel.fetchCaptcha()
    }

    if (viewModel.error != null) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { viewModel.error = null }) {
            androidx.compose.material3.Card(
                shape = RoundedCornerShape(14.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(28.dp)
                ) {

                    Text(
                        text = "Login Error",
                        color = MaterialTheme.colorScheme.primary,
                        style = typography.headlineSmall,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = viewModel.error!!,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        style = typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    PrimaryButton(
                        text = "Ok!",
                        onClick = { viewModel.error = null }
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
        // 🔝 HEADER
        Header(onBack = if (isRefresh) onBack else null)

        Spacer(Modifier.height(60.dp))

        // 🧾 Title
        Text(
            text = if (isRefresh) "Refresh Data" else "Import Data",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = typography.headlineLarge
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = if (isRefresh) "Authenticate to fetch your latest academic results from the university portal."
            else "This is a one-time import of your academic data from the university result portal using your credentials.",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 22.dp),
            textAlign = TextAlign.Center,
            style = typography.bodyLarge.copy(color = Color.Gray)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 📧 EMAIL FIELD
        Text(
            "ENROLLMENT NO",
            style = typography.bodyMedium.copy(
                letterSpacing = 1.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.DarkGray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        AppTextField(
            value = enroll,
            onValueChange = {
                if (!isRefresh && it.length <= 11 && it.all { char -> char.isDigit() }) {
                    enroll = it
                }
            },
            placeholder = "Enter your enrollment number",
            modifier = Modifier.fillMaxWidth(),
            readOnly = isRefresh,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null)
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 📧 Password FIELD
        Text(
            "PASSWORD",
            style = typography.bodyMedium.copy(
                letterSpacing = 1.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.DarkGray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        AppTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Portal Password (default: Father's Name)",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Password, contentDescription = null)
            }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 📧 Captcha FIELD
        Text(
            "CAPTCHA",
            style = typography.bodyMedium.copy(
                letterSpacing = 1.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.DarkGray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (viewModel.captchaError != null) {
            // 🚫 PORTAL UNREACHABLE — calm inline state, rest of the screen stays untouched
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = viewModel.captchaError!!,
                    modifier = Modifier.weight(1f),
                    style = typography.bodySmall.copy(color = Color.DarkGray)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Row(
                    modifier = Modifier
                        .clickable(enabled = !viewModel.captchaLoading) { viewModel.fetchCaptcha() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Retry",
                        style = typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 🧾 INPUT FIELD (takes most space)
                AppTextField(
                    value = captcha,
                    onValueChange = { captcha = it },
                    placeholder = "Enter Captcha",
                    modifier = Modifier.weight(1f),
                    leadingIcon = null   // ✅ no icon here
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 🔄 REFRESH BUTTON
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable(enabled = !viewModel.captchaLoading) {
                            viewModel.fetchCaptcha()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (viewModel.captchaLoading) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 🖼️ CAPTCHA IMAGE
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    base64String?.let { base64 ->
                        val imageBytes = Base64.decode(base64, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        val isFormValid = enroll.length == 11 && password.isNotBlank() && captcha.isNotBlank() &&
            viewModel.sessionId != null

        PrimaryButton(
            text = if (viewModel.loading) {
                if (isRefresh) "Refreshing..." else "Importing..."
            } else {
                if (isRefresh) "Refresh 🔄" else "Import 📥"
            },
            onClick = {
                viewModel.import(
                    username = enroll,
                    password = password,
                    captcha = captcha,
                    onSuccess = {
                        onImportSuccess()
                    }
                )
            },
            enabled = isFormValid && !viewModel.loading
        )

        } // Close inner Column

        // 🔻 Footer
        Footer()
    }
}

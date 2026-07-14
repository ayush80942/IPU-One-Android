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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
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
import com.ipu.ipuoneapp.features.auth.ImportViewModel
import com.ipu.ipuoneapp.features.auth.ImportViewModelFactory

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
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
                shape = RoundedCornerShape(24.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login Error",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = viewModel.error!!,
                        color = androidx.compose.ui.graphics.Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    androidx.compose.material3.TextButton(
                        onClick = { viewModel.error = null }
                    ) {
                        Text(
                            text = "OK",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(WindowInsets.systemBars.asPaddingValues())
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
        // 🔝 HEADER
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isRefresh && onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "IPU One",
                    style = typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    "STUDENT PORTAL",
                    style = typography.labelMedium.copy(
                        letterSpacing = 2.sp
                    )
                )
            }
        }

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
            else "This is a one-time import of your academic data from the university exam portal using your credentials.",
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
                    .clickable {
                        viewModel.fetchCaptcha()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null
                )
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

        Spacer(modifier = Modifier.height(28.dp))

        val isFormValid = enroll.length == 11 && password.isNotBlank() && captcha.isNotBlank()

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
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Need help with your account?", color = Color.Gray)
            Spacer(modifier= Modifier.height(2.dp))
            Text(
                "Contact Student Cell",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    }
}
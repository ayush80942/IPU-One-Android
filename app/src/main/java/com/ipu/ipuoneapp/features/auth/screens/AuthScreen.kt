package com.ipu.ipuoneapp.features.auth.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipu.ipuoneapp.R
import com.ipu.ipuoneapp.core.ui.components.PrimaryButton
import com.ipu.ipuoneapp.core.ui.components.AppTextField
import com.ipu.ipuoneapp.core.ui.components.Header
import com.ipu.ipuoneapp.core.ui.components.Footer
import com.ipu.ipuoneapp.features.auth.AuthViewModel

@Composable
fun AuthScreen(onLoginSuccess: () -> Unit = {} , onSendOtp: (String) -> Unit) {
    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }

    var email by remember { mutableStateOf("") }
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {

        // 🔝 HEADER
        Header()

        Spacer(modifier = Modifier.height(48.dp))

        // 🖼️ LOGO (placeholder for now)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo"
            )
        }

        Spacer(modifier = Modifier.height(60.dp))

        // 📧 EMAIL FIELD
        Text(
            "EMAIL ADDRESS",
            style = typography.bodyMedium.copy(
                letterSpacing = 1.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.DarkGray
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        AppTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter your email address",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 📩 SEND OTP BUTTON
        PrimaryButton(
            text = "Send OTP ➤",
            onClick = {
                viewModel.sendOtp(
                    email = email,
                    onSuccess = {
                        onSendOtp(email)
                    }
                )
            },
            isLoading = state.isLoading,
            enabled = email.trim().isNotEmpty() && !state.isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔀 DIVIDER
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp)
        ) {
            Divider(modifier = Modifier.weight(1f))
            Text(
                text = "  OR  ",
                color = Color.Gray,
                style = typography.labelMedium
            )
            Divider(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        OAuthButton(
            text = "Continue with Google",
            icon = painterResource(id = R.drawable.ic_google),
            onClick = {
                viewModel.signInWithGoogle(context)
            }
        )

        LaunchedEffect(state.token) {
            if (state.token != null) {
                onLoginSuccess()
            }
        }

        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🔻 FOOTER
        Footer()
    }
}

@Composable
fun OAuthButton(
    text: String,
    icon: Painter,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, style = typography.bodyLarge, color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

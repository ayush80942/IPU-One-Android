package com.ipu.ipuoneapp.features.auth.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ipu.ipuoneapp.core.ui.components.PrimaryButton
import com.ipu.ipuoneapp.features.auth.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    email: String,
    onOtpVerified: () -> Unit
) {

    val context = LocalContext.current
    val viewModel = remember { AuthViewModel(context) }
    val state = viewModel.state

    val otpLength = 6
    var otpValue by remember { mutableStateOf("") }
    val shakeOffset = remember { Animatable(0f) }

    var secondsRemaining by remember { mutableStateOf(30) }
    val clipboard = LocalClipboardManager.current

    val otp = otpValue

    // ⏱ Timer
    LaunchedEffect(Unit) {
        while (secondsRemaining > 0) {
            delay(1000)
            secondsRemaining--
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val text = clipboard.getText()?.text

            if (text != null && text.length == 6 && text.all { it.isDigit() }) {
                otpValue = text
                break
            }

            delay(800)
        }
    }

    LaunchedEffect(otp) {
        if (otp.length == otpLength) {
            delay(300)
            viewModel.verifyOtp(email, otp)
        }
    }

    LaunchedEffect(state.error) {
        if (state.error != null) {
            repeat(3) {
                shakeOffset.animateTo(-12f)
                shakeOffset.animateTo(12f)
            }
            shakeOffset.animateTo(0f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔝 HEADER
        Column(
            modifier = Modifier.fillMaxWidth(),
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

        Spacer(Modifier.height(60.dp))

        // 🧾 Title
        Text(
            "Verify Email",
            style = typography.headlineLarge
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "A 6-digit code has been sent to your registered email address.",
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
            style = typography.bodyLarge.copy(color = Color.Gray)
        )

        Spacer(Modifier.height(32.dp))

        // 🔢 OTP BOXES
        BasicTextField(
            value = otpValue,
            onValueChange = {
                if (it.length <= otpLength) {
                    otpValue = it.filter { char -> char.isDigit() }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(x = shakeOffset.value.dp)
                ) {
                    repeat(otpLength) { index ->
                        val char = when {
                            index >= otpValue.length -> ""
                            else -> otpValue[index].toString()
                        }
                        val isFocused = otpValue.length == index || (otpValue.length == otpLength && index == otpLength - 1)
                        val isError = state.error != null

                        Box(
                            modifier = Modifier
                                .width(54.dp)
                                .height(64.dp)
                                .background(Color.White, RoundedCornerShape(18.dp))
                                .border(
                                    width = if (isFocused) 2.dp else 1.dp,
                                    color = when {
                                        isError -> Color.Red
                                        isFocused -> MaterialTheme.colorScheme.primary
                                        else -> Color.LightGray
                                    },
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                style = typography.headlineMedium.copy(
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            )
                        }
                    }
                }
            }
        )

        Spacer(Modifier.height(28.dp))

        // ✅ VERIFY BUTTON
        PrimaryButton(
            text = "Verify Identity ✔",
            onClick = {
                viewModel.verifyOtp(email, otp)
            },
            isLoading = state.isLoading,
            enabled = otp.length == otpLength
        )

        Spacer(Modifier.height(28.dp))

        // ⏱ TIMER / RESEND
        AnimatedContent(
            targetState = secondsRemaining > 0,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
            }
        ) { isTimerVisible ->

            if (isTimerVisible) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            RoundedCornerShape(20.dp)
                        )
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(vertical = 28.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TimerBox(value = 0, label = "MIN")
                        Spacer(Modifier.width(40.dp))
                        TimerBox(value = secondsRemaining, label = "SEC")
                    }
                }

            } else {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        "Didn't receive the code?",
                        style = typography.titleMedium.copy(
                            color = Color.Gray
                        )
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.clickable {
                            secondsRemaining = 30

                            viewModel.sendOtp(
                                email = email,
                                isResend = true
                            )
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            "Resend OTP",
                            style = typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))

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

    // 🔁 Navigation trigger
    LaunchedEffect(state.token) {
        if (state.token != null) {
            onOtpVerified()
        }
    }
}

@Composable
fun TimerBox(
    value: Int,
    label: String
) {
    val animatedValue by animateIntAsState(
        targetValue = value,
        animationSpec = tween(durationMillis = 300)
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(60.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(16.dp)
                )
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                animatedValue.toString().padStart(2, '0'),
                style = typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            label,
            style = typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        )
    }
}
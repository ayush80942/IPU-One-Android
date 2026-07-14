package com.ipu.ipuoneapp.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.ipu.ipuoneapp.core.utils.TokenManager
import com.ipu.ipuoneapp.features.auth.screens.AuthScreen
import com.ipu.ipuoneapp.features.main.MainScreen
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ipu.ipuoneapp.core.network.ApiClient
import com.ipu.ipuoneapp.features.auth.screens.ImportScreen
import com.ipu.ipuoneapp.features.auth.screens.OtpScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }

    val token by tokenManager.token.collectAsState(initial = null)

    var startDestination by remember { mutableStateOf<String?>(null) }

    val api = remember { ApiClient.provideApi(context) }

    LaunchedEffect(token) {

        startDestination = when {

            // ❌ No token → Auth
            token == null -> Routes.Auth.route

            // ❌ Expired token → clear + Auth
            isTokenExpired(token!!) -> {
                tokenManager.clearToken()
                Routes.Auth.route
            }

            // ✅ Token exists → check status
            else -> {
                try {
                    val status = api.getAuthStatus()

                    when {
                        !status.authenticated -> {
                            tokenManager.clearToken()
                            Routes.Auth.route
                        }

                        status.studentLinked -> {
                            Routes.Main.route
                        }

                        else -> {
                            Routes.Import.createRoute(false)   // 🔥 NEW SCREEN
                        }
                    }

                } catch (e: Exception) {
                    tokenManager.clearToken()
                    Routes.Auth.route
                }
            }
        }
    }

    // 🔄 Loader
    if (startDestination == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    NavHost(
        navController = navController,
        startDestination = startDestination!!,
        enterTransition = { 
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)) 
        },
        exitTransition = { 
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300)) 
        },
        popEnterTransition = { 
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)) 
        },
        popExitTransition = { 
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300)) 
        }
    ) {

        composable(Routes.Auth.route) {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Main.route) {
                        popUpTo(Routes.Auth.route) { inclusive = true }
                    }
                },
                onSendOtp = { email ->
                    navController.navigate(Routes.Otp.createRoute(email))
                }
            )
        }

        composable(Routes.Otp.route) { backStackEntry ->

            val email = backStackEntry.arguments?.getString("email") ?: ""

            OtpScreen(
                email = email,
                onOtpVerified = {
                    navController.navigate(Routes.Auth.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Routes.Main.route) {
            MainScreen(
                onNavigateToImport = { enrollStr ->
                    navController.navigate(Routes.Import.createRoute(isRefresh = true, enroll = enrollStr))
                }
            )
        }

        composable(
            route = Routes.Import.route,
            arguments = listOf(
                navArgument("isRefresh") { type = NavType.BoolType; defaultValue = false },
                navArgument("enroll") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val isRefresh = backStackEntry.arguments?.getBoolean("isRefresh") ?: false
            val enroll = backStackEntry.arguments?.getString("enroll") ?: ""
            ImportScreen(
                isRefresh = isRefresh,
                prefilledEnroll = enroll,
                onImportSuccess = {
                    navController.navigate(Routes.Main.route) {
                        popUpTo(Routes.Import.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

fun isTokenExpired(token: String): Boolean {
    return try {
        val payload = token.split(".")[1]

        val decoded = String(
            android.util.Base64.decode(payload, android.util.Base64.URL_SAFE)
        )

        val json = org.json.JSONObject(decoded)

        val exp = json.getLong("exp") // seconds
        val now = System.currentTimeMillis() / 1000

        now >= exp

    } catch (e: Exception) {
        true
    }
}
package com.ipu.ipuoneapp.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.ipu.ipuoneapp.core.network.ApiClient
import com.ipu.ipuoneapp.data.model.auth.AuthStatusResponse
import com.ipu.ipuoneapp.navigation.Routes
import com.ipu.ipuoneapp.features.home.HomeScreen
import com.ipu.ipuoneapp.features.notices.NoticesScreen
import com.ipu.ipuoneapp.features.results.ResultsScreen
import com.ipu.ipuoneapp.features.profile.ProfileScreen
import com.ipu.ipuoneapp.features.services.ServicesScreen
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

@Composable
fun MainScreen(
    onNavigateToImport: (String) -> Unit = {}
) {

    val navController = rememberNavController()

    val items = listOf(
        Routes.Home,
        Routes.Notices,
        Routes.Services,
        Routes.Results,
        Routes.Profile
    )

    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Notifications,
        Icons.Default.DocumentScanner,
        Icons.Default.Dashboard,
        Icons.Default.Person
    )

    val labels = listOf("Home", "Notices", "Collect", "Results", "Profile")

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    val api = remember { ApiClient.provideApi(context) }

    var data by remember { mutableStateOf<AuthStatusResponse?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val res = api.getAuthStatus()
            data = res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        loading = false
    }

    Scaffold(
        bottomBar = {
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    windowInsets = WindowInsets(top = 1.dp, bottom = 10.dp)
                ) {
                    items.forEachIndexed { index, route ->
                        val selected = currentRoute == route.route

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(route.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = icons[index],
                                    contentDescription = labels[index],
                                    modifier = Modifier.size(if (selected) 26.dp else 22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = labels[index],
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Gray.copy(alpha = 0.6f),
                                unselectedTextColor = Color.Gray.copy(alpha = 0.6f),
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(padding),
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

            composable(Routes.Home.route) {

                if (loading) {
                    Text("Loading...")
                } else if (data != null && data!!.authenticated && data!!.studentLinked) {
                    HomeScreen(
                        data = data!!,
                        onViewAllNotices = {
                            navController.navigate(Routes.Notices.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        onNavigateToResults = {
                            navController.navigate(Routes.Results.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        onNavigateToCollect = {
                            navController.navigate(Routes.Services.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    )
                } else {
                    Text("Not ready")
                }
            }
            composable(Routes.Notices.route) { NoticesScreen() }
            composable(Routes.Services.route) { 
                com.ipu.ipuoneapp.features.services.collect.CollectDocumentScreen()
            }
            composable(Routes.Results.route) { 
                ResultsScreen(
                    onNavigateToImport = { 
                        onNavigateToImport(data?.enrollmentNo ?: "") 
                    }
                ) 
            }
            composable(Routes.Profile.route) { ProfileScreen() }

        }
    }
}
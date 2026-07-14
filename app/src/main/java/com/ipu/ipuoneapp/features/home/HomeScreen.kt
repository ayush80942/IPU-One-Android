package com.ipu.ipuoneapp.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ipu.ipuoneapp.data.model.auth.AuthStatusResponse
import com.ipu.ipuoneapp.features.home.components.HomeHeader
import com.ipu.ipuoneapp.features.home.components.HomeResultStatsSection
import com.ipu.ipuoneapp.features.home.components.LatestNoticesSection
import com.ipu.ipuoneapp.features.home.components.RecentDocumentsSection
import com.ipu.ipuoneapp.features.home.components.StudentCard

@Composable
fun HomeScreen(
    data: AuthStatusResponse,
    onViewAllNotices: () -> Unit = {},
    onNavigateToResults: () -> Unit = {},
    onNavigateToCollect: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                WindowInsets.safeDrawing
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
                    .asPaddingValues()
            )
            .padding(top = 10.dp, start = 16.dp, end = 16.dp)
    ) {
        HomeHeader(name = data.name ?: "")
        Spacer(modifier = Modifier.height(6.dp))
        StudentCard(data)
        Spacer(modifier = Modifier.height(24.dp))
        HomeResultStatsSection(onNavigateToResults = onNavigateToResults)
        Spacer(modifier = Modifier.height(24.dp))
        LatestNoticesSection(onViewAll = onViewAllNotices)
        Spacer(modifier = Modifier.height(24.dp))
        RecentDocumentsSection(onViewAll = onNavigateToCollect)
        Spacer(modifier = Modifier.height(24.dp))
    }
}
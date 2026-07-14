package com.ipu.ipuoneapp.features.results

import com.ipu.ipuoneapp.data.model.result.ResultDashboardDTO

data class ResultsState(
    val isLoading: Boolean = false,
    val data: ResultDashboardDTO? = null,
    val error: String? = null
)
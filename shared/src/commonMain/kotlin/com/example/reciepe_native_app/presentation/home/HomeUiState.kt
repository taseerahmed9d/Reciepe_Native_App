package com.example.reciepe_native_app.presentation.home

import com.example.reciepe_native_app.domain.model.RecipeSummary

data class HomeUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val recipes: List<RecipeSummary> = emptyList(),
    val error: String? = null,
    val isOffline: Boolean = false,
)

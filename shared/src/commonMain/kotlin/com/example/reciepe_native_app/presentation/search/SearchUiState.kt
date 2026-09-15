package com.example.reciepe_native_app.presentation.search

import com.example.reciepe_native_app.domain.model.RecipeSummary

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<RecipeSummary> = emptyList(),
    val error: String? = null,
    val isOffline: Boolean = false,
)

package com.example.reciepe_native_app.presentation.favorites

import com.example.reciepe_native_app.domain.model.RecipeSummary

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val recipes: List<RecipeSummary> = emptyList(),
)

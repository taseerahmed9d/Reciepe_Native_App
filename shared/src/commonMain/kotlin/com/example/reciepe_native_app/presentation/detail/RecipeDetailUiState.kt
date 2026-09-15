package com.example.reciepe_native_app.presentation.detail

import com.example.reciepe_native_app.domain.model.Recipe

data class RecipeDetailUiState(
    val isLoading: Boolean = true,
    val recipe: Recipe? = null,
    val error: String? = null,
    val isOffline: Boolean = false,
)

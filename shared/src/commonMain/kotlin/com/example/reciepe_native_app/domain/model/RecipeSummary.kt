package com.example.reciepe_native_app.domain.model

data class RecipeSummary(
    val id: String,
    val name: String,
    val thumbnailUrl: String?,
    val category: String?,
    val area: String?,
    val isFavorite: Boolean,
)

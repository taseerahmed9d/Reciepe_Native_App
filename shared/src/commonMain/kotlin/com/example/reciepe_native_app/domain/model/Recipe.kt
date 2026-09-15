package com.example.reciepe_native_app.domain.model

data class Recipe(
    val id: String,
    val name: String,
    val category: String?,
    val area: String?,
    val instructions: String?,
    val thumbnailUrl: String?,
    val youtubeUrl: String?,
    val ingredients: List<Ingredient>,
    val isFavorite: Boolean,
    val isHomeList: Boolean,
    val cachedAt: Long,
) {
    fun toSummary(): RecipeSummary = RecipeSummary(
        id = id,
        name = name,
        thumbnailUrl = thumbnailUrl,
        category = category,
        area = area,
        isFavorite = isFavorite,
    )
}

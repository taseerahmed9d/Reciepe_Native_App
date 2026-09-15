package com.example.reciepe_native_app.data.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class MealsResponse(
    val meals: List<JsonObject>? = null,
)

@Serializable
data class CategoriesResponse(
    val categories: List<CategoryDto>? = null,
)

@Serializable
data class CategoryDto(
    val idCategory: String? = null,
    val strCategory: String? = null,
    val strCategoryThumb: String? = null,
    val strCategoryDescription: String? = null,
)

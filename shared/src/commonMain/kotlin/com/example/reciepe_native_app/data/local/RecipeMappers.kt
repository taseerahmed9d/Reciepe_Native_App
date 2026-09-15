package com.example.reciepe_native_app.data.local

import com.example.reciepe_native_app.data.remote.appJson
import com.example.reciepe_native_app.domain.model.Ingredient
import com.example.reciepe_native_app.domain.model.Recipe
import com.example.reciepe_native_app.domain.model.RecipeSummary
import kotlinx.serialization.builtins.ListSerializer

internal fun RecipeEntity.toDomain(): Recipe = Recipe(
    id = id,
    name = name,
    category = category,
    area = area,
    instructions = instructions,
    thumbnailUrl = thumbnailUrl,
    youtubeUrl = youtubeUrl,
    ingredients = decodeIngredients(ingredientsJson),
    isFavorite = isFavorite,
    isHomeList = isHomeList,
    cachedAt = cachedAt,
)

internal fun RecipeEntity.toSummary(): RecipeSummary = RecipeSummary(
    id = id,
    name = name,
    thumbnailUrl = thumbnailUrl,
    category = category,
    area = area,
    isFavorite = isFavorite,
)

internal fun SearchCacheEntity.toSummary(isFavorite: Boolean = false): RecipeSummary = RecipeSummary(
    id = mealId,
    name = name,
    thumbnailUrl = thumbnailUrl,
    category = null,
    area = null,
    isFavorite = isFavorite,
)

internal fun Recipe.toEntity(cachedAt: Long): RecipeEntity = RecipeEntity(
    id = id,
    name = name,
    category = category,
    area = area,
    instructions = instructions,
    thumbnailUrl = thumbnailUrl,
    youtubeUrl = youtubeUrl,
    ingredientsJson = encodeIngredients(ingredients),
    isFavorite = isFavorite,
    isHomeList = isHomeList,
    cachedAt = cachedAt,
)

internal fun encodeIngredients(ingredients: List<Ingredient>): String =
    appJson.encodeToString(ListSerializer(Ingredient.serializer()), ingredients)

internal fun decodeIngredients(json: String): List<Ingredient> =
    if (json.isBlank()) {
        emptyList()
    } else {
        runCatching {
            appJson.decodeFromString(ListSerializer(Ingredient.serializer()), json)
        }.getOrDefault(emptyList())
    }

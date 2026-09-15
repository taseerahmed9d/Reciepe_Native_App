package com.example.reciepe_native_app.data.remote

import com.example.reciepe_native_app.domain.model.Ingredient
import com.example.reciepe_native_app.domain.model.Recipe
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal fun JsonObject.toRecipe(
    isFavorite: Boolean = false,
    isHomeList: Boolean = false,
    cachedAt: Long = Clock.System.now().toEpochMilliseconds(),
): Recipe? {
    val id = string("idMeal") ?: return null
    val name = string("strMeal") ?: return null
    return Recipe(
        id = id,
        name = name,
        category = string("strCategory"),
        area = string("strArea"),
        instructions = string("strInstructions"),
        thumbnailUrl = string("strMealThumb"),
        youtubeUrl = string("strYoutube"),
        ingredients = ingredients(),
        isFavorite = isFavorite,
        isHomeList = isHomeList,
        cachedAt = cachedAt,
    )
}

private fun JsonObject.string(key: String): String? =
    this[key]?.jsonPrimitive?.contentOrNull?.trim()?.takeIf { it.isNotEmpty() && !it.equals("null", ignoreCase = true) }

private fun JsonObject.ingredients(): List<Ingredient> = buildList {
    for (index in 1..20) {
        val name = string("strIngredient$index") ?: continue
        val measure = string("strMeasure$index").orEmpty()
        add(Ingredient(name = name, measure = measure))
    }
}

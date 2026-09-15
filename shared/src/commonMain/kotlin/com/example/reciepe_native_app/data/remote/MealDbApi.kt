package com.example.reciepe_native_app.data.remote

import com.example.reciepe_native_app.domain.model.Recipe
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MealDbApi(
    private val client: HttpClient,
) {
    suspend fun searchMeals(query: String): List<Recipe> {
        val response: MealsResponse = client.get("search.php") {
            parameter("s", query)
        }.body()
        return response.meals.orEmpty().mapNotNull { it.toRecipe() }
    }

    suspend fun lookupMeal(id: String): Recipe? {
        val response: MealsResponse = client.get("lookup.php") {
            parameter("i", id)
        }.body()
        return response.meals.orEmpty().firstNotNullOfOrNull { it.toRecipe() }
    }

    suspend fun getCategories(): List<CategoryDto> {
        val response: CategoriesResponse = client.get("categories.php").body()
        return response.categories.orEmpty()
    }
}

package com.example.reciepe_native_app.data.repository

import com.example.reciepe_native_app.domain.model.Recipe
import com.example.reciepe_native_app.domain.model.RecipeSummary
import kotlinx.coroutines.flow.Flow

data class RecipeListUpdate(
    val recipes: List<RecipeSummary>,
    val isOffline: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
)

data class RecipeDetailUpdate(
    val recipe: Recipe?,
    val isOffline: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false,
)

interface RecipeRepository {
    fun getHomeList(): Flow<RecipeListUpdate>
    fun search(query: String): Flow<RecipeListUpdate>
    fun getRecipeDetail(id: String): Flow<RecipeDetailUpdate>
    fun observeFavorites(): Flow<List<RecipeSummary>>
    suspend fun toggleFavorite(id: String)
}

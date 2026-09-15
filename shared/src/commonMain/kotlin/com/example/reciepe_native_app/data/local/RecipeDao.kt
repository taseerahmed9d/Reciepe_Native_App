package com.example.reciepe_native_app.data.local

import com.example.reciepe_native_app.domain.model.Recipe
import com.example.reciepe_native_app.domain.model.RecipeSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class RecipeDao(
    private val roomDao: RecipeRoomDao,
) {
    fun observeHomeList(): Flow<List<RecipeSummary>> =
        roomDao.observeHomeList().map { rows -> rows.map { it.toSummary() } }

    fun observeFavorites(): Flow<List<RecipeSummary>> =
        roomDao.observeFavorites().map { rows -> rows.map { it.toSummary() } }

    fun observeById(id: String): Flow<Recipe?> =
        roomDao.observeById(id).map { it?.toDomain() }

    suspend fun selectById(id: String): Recipe? =
        roomDao.selectById(id)?.toDomain()

    @OptIn(ExperimentalTime::class)
    suspend fun upsertRecipes(
        recipes: List<Recipe>,
        markAsHomeList: Boolean = false,
    ) {
        val cachedAt = Clock.System.now().toEpochMilliseconds()
        roomDao.upsertRecipes(
            recipes = recipes.map { it.toEntity(cachedAt = cachedAt) },
            markAsHomeList = markAsHomeList,
        )
    }

    @OptIn(ExperimentalTime::class)
    suspend fun upsertRecipe(recipe: Recipe) {
        val cachedAt = Clock.System.now().toEpochMilliseconds()
        roomDao.upsertRecipe(recipe.toEntity(cachedAt = cachedAt))
    }

    suspend fun setFavorite(id: String, isFavorite: Boolean) {
        roomDao.setFavorite(isFavorite = isFavorite, id = id)
    }

    @OptIn(ExperimentalTime::class)
    suspend fun saveSearchResults(query: String, recipes: List<Recipe>) {
        val cachedAt = Clock.System.now().toEpochMilliseconds()
        roomDao.saveSearchResults(
            query = query,
            cacheRows = recipes.map { recipe ->
                SearchCacheEntity(
                    query = query,
                    mealId = recipe.id,
                    name = recipe.name,
                    thumbnailUrl = recipe.thumbnailUrl,
                    cachedAt = cachedAt,
                )
            },
            recipes = recipes.map { it.toEntity(cachedAt = cachedAt) },
        )
    }

    suspend fun searchCache(query: String): List<RecipeSummary> {
        val exact = roomDao.selectSearchCache(query)
        val rows = if (exact.isNotEmpty()) {
            exact
        } else {
            roomDao.searchCacheByName(query)
        }
        return rows.distinctBy { it.mealId }.map { cache ->
            val favorite = roomDao.selectById(cache.mealId)?.isFavorite == true
            cache.toSummary(isFavorite = favorite)
        }
    }
}

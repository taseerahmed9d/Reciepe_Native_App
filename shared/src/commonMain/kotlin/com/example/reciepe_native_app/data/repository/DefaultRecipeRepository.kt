package com.example.reciepe_native_app.data.repository

import com.example.reciepe_native_app.data.local.RecipeDao
import com.example.reciepe_native_app.data.remote.MealDbApi
import com.example.reciepe_native_app.domain.model.RecipeSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal class DefaultRecipeRepository(
    private val api: MealDbApi,
    private val dao: RecipeDao,
) : RecipeRepository {

    override fun getHomeList(): Flow<RecipeListUpdate> = channelFlow {
        val refreshing = MutableStateFlow(true)
        val network = MutableStateFlow(NetworkStatus())

        launch {
            try {
                val remote = api.searchMeals(query = "")
                dao.upsertRecipes(remote, markAsHomeList = true)
                network.value = NetworkStatus()
            } catch (t: Throwable) {
                network.value = NetworkStatus(
                    isOffline = true,
                    error = t.toUserMessage(),
                )
            } finally {
                refreshing.value = false
            }
        }

        combine(
            dao.observeHomeList(),
            refreshing,
            network,
        ) { recipes, isRefreshing, status ->
            RecipeListUpdate(
                recipes = recipes,
                isOffline = status.isOffline,
                error = status.error,
                isRefreshing = isRefreshing,
            )
        }.collect { send(it) }
    }

    override fun search(query: String): Flow<RecipeListUpdate> = flow {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            emit(RecipeListUpdate(recipes = emptyList()))
            return@flow
        }
        emit(RecipeListUpdate(recipes = emptyList(), isRefreshing = true))
        try {
            val remote = api.searchMeals(trimmed)
            dao.saveSearchResults(trimmed, remote)
            emit(
                RecipeListUpdate(
                    recipes = remote.map { it.toSummary() },
                    isRefreshing = false,
                ),
            )
        }
        catch (t: Throwable) {
            val cached = dao.searchCache(trimmed)
            emit(
                RecipeListUpdate(
                    recipes = cached,
                    isOffline = true,
                    error = t.toUserMessage(),
                    isRefreshing = false,
                ),
            )
        }
    }

    override fun getRecipeDetail(id: String): Flow<RecipeDetailUpdate> = channelFlow {
        val refreshing = MutableStateFlow(true)
        val network = MutableStateFlow(NetworkStatus())

        launch {
            try {
                val remote = api.lookupMeal(id)
                if (remote != null) {
                    dao.upsertRecipe(remote)
                }
                network.value = NetworkStatus()
            }
            catch (t: Throwable) {
                network.value = NetworkStatus(
                    isOffline = true,
                    error = t.toUserMessage(),
                )
            }
            finally {
                refreshing.value = false
            }
        }

        combine(
            dao.observeById(id),
            refreshing,
            network,
        ) { recipe, isRefreshing, status ->
            RecipeDetailUpdate(
                recipe = recipe,
                isOffline = status.isOffline,
                error = status.error,
                isRefreshing = isRefreshing,
            )
        }.collect { send(it) }
    }

    override fun observeFavorites(): Flow<List<RecipeSummary>> = dao.observeFavorites()

    override suspend fun toggleFavorite(id: String) {
        val current = dao.selectById(id) ?: return
        dao.setFavorite(id, !current.isFavorite)
    }
}

private data class NetworkStatus(
    val isOffline: Boolean = false,
    val error: String? = null,
)

private fun Throwable.toUserMessage(): String =
    message?.takeIf { it.isNotBlank() } ?: "Unable to reach the recipe service."

package com.example.reciepe_native_app.di

import com.example.reciepe_native_app.presentation.detail.RecipeDetailUiState
import com.example.reciepe_native_app.presentation.detail.RecipeDetailViewModel
import com.example.reciepe_native_app.presentation.favorites.FavoritesUiState
import com.example.reciepe_native_app.presentation.favorites.FavoritesViewModel
import com.example.reciepe_native_app.presentation.home.HomeUiState
import com.example.reciepe_native_app.presentation.home.HomeViewModel
import com.example.reciepe_native_app.presentation.search.SearchUiState
import com.example.reciepe_native_app.presentation.search.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FlowWatcher internal constructor(
    private val close: () -> Unit,
) {
    fun close() = close.invoke()
}

private fun watch(
    collect: suspend () -> Unit,
): FlowWatcher {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    val job = scope.launch { collect() }
    return FlowWatcher {
        job.cancel()
        scope.cancel()
    }
}

fun watchHomeState(
    viewModel: HomeViewModel,
    onEach: (HomeUiState) -> Unit,
): FlowWatcher = watch {
    viewModel.state.collect(onEach)
}

fun HomeViewModel.currentState(): HomeUiState = state.value

fun SearchViewModel.currentState(): SearchUiState = state.value

fun RecipeDetailViewModel.currentState(): RecipeDetailUiState = state.value

fun FavoritesViewModel.currentState(): FavoritesUiState = state.value

fun watchSearchState(
    viewModel: SearchViewModel,
    onEach: (SearchUiState) -> Unit,
): FlowWatcher = watch {
    viewModel.state.collect(onEach)
}

fun watchDetailState(
    viewModel: RecipeDetailViewModel,
    onEach: (RecipeDetailUiState) -> Unit,
): FlowWatcher = watch {
    viewModel.state.collect(onEach)
}

fun watchFavoritesState(
    viewModel: FavoritesViewModel,
    onEach: (FavoritesUiState) -> Unit,
): FlowWatcher = watch {
    viewModel.state.collect(onEach)
}

package com.example.reciepe_native_app.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reciepe_native_app.data.repository.RecipeRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailViewModel(
    private val recipeId: String,
    private val repository: RecipeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeDetailUiState())

    @NativeCoroutinesState
    val state: StateFlow<RecipeDetailUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getRecipeDetail(recipeId).collect { update ->
                _state.update {
                    it.copy(
                        isLoading = update.isRefreshing && update.recipe == null,
                        recipe = update.recipe,
                        error = update.error,
                        isOffline = update.isOffline,
                    )
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            repository.toggleFavorite(recipeId)
        }
    }
}

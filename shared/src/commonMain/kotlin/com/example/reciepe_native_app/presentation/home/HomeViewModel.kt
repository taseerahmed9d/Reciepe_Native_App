package com.example.reciepe_native_app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reciepe_native_app.data.repository.RecipeRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: RecipeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())

    @NativeCoroutinesState
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    private var observeJob: Job? = null

    init {
        observeHomeList()
    }

    fun refresh() {
        observeHomeList()
    }

    private fun observeHomeList() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            repository.getHomeList().collect { update ->
                _state.update {
                    it.copy(
                        isLoading = update.isRefreshing && update.recipes.isEmpty(),
                        isRefreshing = update.isRefreshing,
                        recipes = update.recipes,
                        error = update.error,
                        isOffline = update.isOffline,
                    )
                }
            }
        }
    }

}

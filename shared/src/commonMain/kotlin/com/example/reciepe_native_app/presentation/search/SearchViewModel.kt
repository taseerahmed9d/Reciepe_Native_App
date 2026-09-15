package com.example.reciepe_native_app.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reciepe_native_app.data.repository.RecipeRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: RecipeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())

    @NativeCoroutinesState
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private val query = MutableStateFlow("")

    init {
        observeQuery()
    }

    fun onQueryChange(value: String) {
        query.value = value
        _state.update { it.copy(query = value) }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeQuery() {
        viewModelScope.launch {
            query
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { raw ->
                    repository.search(raw)
                }
                .collect { update ->
                    _state.update { current ->
                        current.copy(
                            isLoading = update.isRefreshing,
                            results = update.recipes,
                            error = update.error,
                            isOffline = update.isOffline,
                        )
                    }
                }
        }
    }
}

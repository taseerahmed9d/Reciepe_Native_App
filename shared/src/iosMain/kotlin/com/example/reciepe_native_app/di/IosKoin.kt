package com.example.reciepe_native_app.di

import com.example.reciepe_native_app.data.local.getDatabaseBuilder
import com.example.reciepe_native_app.presentation.detail.RecipeDetailViewModel
import com.example.reciepe_native_app.presentation.favorites.FavoritesViewModel
import com.example.reciepe_native_app.presentation.home.HomeViewModel
import com.example.reciepe_native_app.presentation.search.SearchViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val iosPlatformModule = module {
    single { getDatabaseBuilder() }
}

fun doInitKoin() {
    initKoin(extraModules = listOf(iosPlatformModule))
}

class IosViewModelFactory : KoinComponent {
    fun homeViewModel(): HomeViewModel = get()
    fun searchViewModel(): SearchViewModel = get()
    fun recipeDetailViewModel(id: String): RecipeDetailViewModel = get { parametersOf(id) }
    fun favoritesViewModel(): FavoritesViewModel = get()
}

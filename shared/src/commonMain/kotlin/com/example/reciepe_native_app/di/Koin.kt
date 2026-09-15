package com.example.reciepe_native_app.di

import com.example.reciepe_native_app.data.local.AppDatabase
import com.example.reciepe_native_app.data.local.RecipeDao
import com.example.reciepe_native_app.data.local.getRoomDatabase
import com.example.reciepe_native_app.data.remote.MealDbApi
import com.example.reciepe_native_app.data.remote.createHttpClient
import com.example.reciepe_native_app.data.repository.DefaultRecipeRepository
import com.example.reciepe_native_app.data.repository.RecipeRepository
import com.example.reciepe_native_app.presentation.detail.RecipeDetailViewModel
import com.example.reciepe_native_app.presentation.favorites.FavoritesViewModel
import com.example.reciepe_native_app.presentation.home.HomeViewModel
import com.example.reciepe_native_app.presentation.search.SearchViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    extraModules: List<Module> = emptyList(),
    appDeclaration: KoinAppDeclaration = {},
) {
    startKoin {
        appDeclaration()
        modules(listOf(sharedModule) + extraModules)
    }
}

val sharedModule: Module = module {
    single { createHttpClient() }
    single { MealDbApi(get()) }
    single { getRoomDatabase(get()) }
    single { get<AppDatabase>().recipeDao() }
    single { RecipeDao(get()) }
    single<RecipeRepository> { DefaultRecipeRepository(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { params -> RecipeDetailViewModel(params.get(), get()) }
    viewModel { FavoritesViewModel(get()) }
}

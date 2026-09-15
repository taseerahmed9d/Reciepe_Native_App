package com.example.reciepe_native_app

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reciepe_native_app.ui.detail.RecipeDetailScreen
import com.example.reciepe_native_app.ui.favorites.FavoritesScreen
import com.example.reciepe_native_app.ui.home.HomeScreen
import com.example.reciepe_native_app.ui.search.SearchScreen

private enum class DesktopDest {
    Home,
    Search,
    Favorites,
}

@Composable
fun DesktopRoot() {
    var dest by rememberSaveable { mutableStateOf(DesktopDest.Home) }
    var selectedRecipeId by rememberSaveable { mutableStateOf<String?>(null) }

    Row(Modifier.fillMaxSize()) {
        NavigationRail {
            NavigationRailItem(
                selected = dest == DesktopDest.Home && selectedRecipeId == null,
                onClick = {
                    dest = DesktopDest.Home
                    selectedRecipeId = null
                },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
            )
            NavigationRailItem(
                selected = dest == DesktopDest.Search && selectedRecipeId == null,
                onClick = {
                    dest = DesktopDest.Search
                    selectedRecipeId = null
                },
                icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                label = { Text("Search") },
            )
            NavigationRailItem(
                selected = dest == DesktopDest.Favorites && selectedRecipeId == null,
                onClick = {
                    dest = DesktopDest.Favorites
                    selectedRecipeId = null
                },
                icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                label = { Text("Favorites") },
            )
        }
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(end = 8.dp)
        val recipeId = selectedRecipeId
        if (recipeId != null) {
            RecipeDetailScreen(
                recipeId = recipeId,
                onBack = { selectedRecipeId = null },
                modifier = contentModifier,
            )
        } else {
            when (dest) {
                DesktopDest.Home -> HomeScreen(
                    onRecipeClick = { selectedRecipeId = it },
                    modifier = contentModifier,
                )
                DesktopDest.Search -> SearchScreen(
                    onRecipeClick = { selectedRecipeId = it },
                    modifier = contentModifier,
                )
                DesktopDest.Favorites -> FavoritesScreen(
                    onRecipeClick = { selectedRecipeId = it },
                    modifier = contentModifier,
                )
            }
        }
    }
}

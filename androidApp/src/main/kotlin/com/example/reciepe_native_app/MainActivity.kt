package com.example.reciepe_native_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.reciepe_native_app.ui.detail.RecipeDetailScreen
import com.example.reciepe_native_app.ui.favorites.FavoritesScreen
import com.example.reciepe_native_app.ui.home.HomeScreen
import com.example.reciepe_native_app.ui.search.SearchScreen
import com.example.reciepe_native_app.ui.theme.RecipeBoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            RecipeBoxTheme {
                RecipeBoxApp()
            }
        }
    }
}

private data class TopLevelRoute(
    val route: String,
    val labelRes: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

@Composable
fun RecipeBoxApp() {
    val navController = rememberNavController()
    val destinations = listOf(
        TopLevelRoute("home", R.string.home, Icons.Default.Home),
        TopLevelRoute("search", R.string.search, Icons.Default.Search),
        TopLevelRoute("favorites", R.string.favorites, Icons.Default.Favorite),
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = destinations.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    destinations.forEach { dest ->
                        NavigationBarItem(
                            selected = currentRoute == dest.route,
                            onClick = {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(dest.icon, contentDescription = stringResource(dest.labelRes)) },
                            label = { Text(stringResource(dest.labelRes)) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("home") {
                HomeScreen(onRecipeClick = { navController.navigate("detail/$it") })
            }
            composable("search") {
                SearchScreen(onRecipeClick = { navController.navigate("detail/$it") })
            }
            composable("favorites") {
                FavoritesScreen(onRecipeClick = { navController.navigate("detail/$it") })
            }
            composable(
                route = "detail/{recipeId}",
                arguments = listOf(navArgument("recipeId") { type = NavType.StringType }),
            ) { entry ->
                val recipeId = entry.arguments?.getString("recipeId").orEmpty()
                RecipeDetailScreen(
                    recipeId = recipeId,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}

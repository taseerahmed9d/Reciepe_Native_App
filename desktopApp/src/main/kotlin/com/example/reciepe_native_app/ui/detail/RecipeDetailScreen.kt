package com.example.reciepe_native_app.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.reciepe_native_app.presentation.detail.RecipeDetailViewModel
import com.example.reciepe_native_app.ui.components.EmptyState
import com.example.reciepe_native_app.ui.components.OfflineBanner
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RecipeDetailViewModel = koinViewModel { parametersOf(recipeId) },
) {
    val state by viewModel.state.collectAsState()
    val recipe = state.recipe

    Column(modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(recipe?.name ?: "Recipe") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = viewModel::toggleFavorite, enabled = recipe != null) {
                    Icon(
                        imageVector = if (recipe?.isFavorite == true) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = if (recipe?.isFavorite == true) {
                            "Remove from favorites"
                        } else {
                            "Favorite"
                        },
                    )
                }
            },
        )
        OfflineBanner(visible = state.isOffline)
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            recipe == null -> EmptyState(state.error ?: "Recipe not found.")
            else -> {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                ) {
                    AsyncImage(
                        model = recipe.thumbnailUrl,
                        contentDescription = recipe.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.8f),
                    )
                    Column(Modifier.padding(20.dp)) {
                        val meta = listOfNotNull(recipe.category, recipe.area).joinToString(" · ")
                        if (meta.isNotBlank()) {
                            Text(
                                text = meta,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                        Text("Ingredients", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))
                        recipe.ingredients.forEach { ingredient ->
                            var checked by rememberSaveable(ingredient.name, ingredient.measure) {
                                mutableStateOf(false)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Checkbox(checked = checked, onCheckedChange = { checked = it })
                                Text(
                                    text = listOf(ingredient.measure, ingredient.name)
                                        .filter { it.isNotBlank() }
                                        .joinToString(" "),
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text("Instructions", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = recipe.instructions.orEmpty(),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}

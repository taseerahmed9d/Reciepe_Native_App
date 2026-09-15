package com.example.reciepe_native_app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipe")
internal data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String?,
    val area: String?,
    val instructions: String?,
    val thumbnailUrl: String?,
    val youtubeUrl: String?,
    val ingredientsJson: String,
    val isFavorite: Boolean,
    val isHomeList: Boolean,
    val cachedAt: Long,
)

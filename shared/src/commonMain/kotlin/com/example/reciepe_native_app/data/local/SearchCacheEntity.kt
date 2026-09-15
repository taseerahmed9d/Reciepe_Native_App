package com.example.reciepe_native_app.data.local

import androidx.room.Entity

@Entity(
    tableName = "SearchCache",
    primaryKeys = ["query", "mealId"],
)
internal data class SearchCacheEntity(
    val query: String,
    val mealId: String,
    val name: String,
    val thumbnailUrl: String?,
    val cachedAt: Long,
)

package com.example.reciepe_native_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class RecipeRoomDao {
    @Query(
        """
        SELECT * FROM Recipe
        WHERE isHomeList = 1
        ORDER BY name COLLATE NOCASE
        """,
    )
    abstract fun observeHomeList(): Flow<List<RecipeEntity>>

    @Query(
        """
        SELECT * FROM Recipe
        WHERE isFavorite = 1
        ORDER BY name COLLATE NOCASE
        """,
    )
    abstract fun observeFavorites(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM Recipe WHERE id = :id")
    abstract fun observeById(id: String): Flow<RecipeEntity?>

    @Query("SELECT * FROM Recipe WHERE id = :id")
    abstract suspend fun selectById(id: String): RecipeEntity?

    @Insert
    abstract suspend fun insertRecipe(entity: RecipeEntity)

    @Query(
        """
        UPDATE Recipe SET
            name = :name,
            category = :category,
            area = :area,
            instructions = :instructions,
            thumbnailUrl = :thumbnailUrl,
            youtubeUrl = :youtubeUrl,
            ingredientsJson = :ingredientsJson,
            cachedAt = :cachedAt
        WHERE id = :id
        """,
    )
    abstract suspend fun updateRecipeContent(
        name: String,
        category: String?,
        area: String?,
        instructions: String?,
        thumbnailUrl: String?,
        youtubeUrl: String?,
        ingredientsJson: String,
        cachedAt: Long,
        id: String,
    )

    @Query("UPDATE Recipe SET isFavorite = :isFavorite WHERE id = :id")
    abstract suspend fun setFavorite(isFavorite: Boolean, id: String)

    @Query("UPDATE Recipe SET isHomeList = :isHomeList WHERE id = :id")
    abstract suspend fun setInHomeList(isHomeList: Boolean, id: String)

    @Query("UPDATE Recipe SET isHomeList = 0")
    abstract suspend fun clearHomeList()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSearchCache(entity: SearchCacheEntity)

    @Query("DELETE FROM SearchCache WHERE `query` = :query")
    abstract suspend fun deleteSearchCache(query: String)

    @Query(
        """
        SELECT * FROM SearchCache
        WHERE `query` = :query
        ORDER BY name COLLATE NOCASE
        """,
    )
    abstract suspend fun selectSearchCache(query: String): List<SearchCacheEntity>

    @Query(
        """
        SELECT * FROM SearchCache
        WHERE name LIKE '%' || :query || '%'
        ORDER BY name COLLATE NOCASE
        """,
    )
    abstract suspend fun searchCacheByName(query: String): List<SearchCacheEntity>

    @Transaction
    open suspend fun upsertRecipes(
        recipes: List<RecipeEntity>,
        markAsHomeList: Boolean,
    ) {
        if (markAsHomeList) {
            clearHomeList()
        }
        recipes.forEach { recipe ->
            upsertRow(
                recipe = recipe,
                isHomeList = markAsHomeList || recipe.isHomeList,
            )
            if (markAsHomeList) {
                setInHomeList(true, recipe.id)
            }
        }
    }

    @Transaction
    open suspend fun upsertRecipe(recipe: RecipeEntity) {
        upsertRow(recipe = recipe, isHomeList = recipe.isHomeList)
    }

    @Transaction
    open suspend fun saveSearchResults(
        query: String,
        cacheRows: List<SearchCacheEntity>,
        recipes: List<RecipeEntity>,
    ) {
        deleteSearchCache(query)
        cacheRows.forEach { insertSearchCache(it) }
        recipes.forEach { recipe ->
            upsertRow(recipe = recipe, isHomeList = false)
        }
    }

    private suspend fun upsertRow(recipe: RecipeEntity, isHomeList: Boolean) {
        val existing = selectById(recipe.id)
        if (existing == null) {
            insertRecipe(recipe.copy(isHomeList = isHomeList))
        } else {
            updateRecipeContent(
                name = recipe.name,
                category = recipe.category,
                area = recipe.area,
                instructions = recipe.instructions,
                thumbnailUrl = recipe.thumbnailUrl,
                youtubeUrl = recipe.youtubeUrl,
                ingredientsJson = recipe.ingredientsJson,
                cachedAt = recipe.cachedAt,
                id = recipe.id,
            )
        }
    }
}

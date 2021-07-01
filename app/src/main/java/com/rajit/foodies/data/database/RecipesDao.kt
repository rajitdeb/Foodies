package com.rajit.foodies.data.database

import androidx.room.*
import com.rajit.foodies.data.database.entities.FavouritesEntity
import com.rajit.foodies.data.database.entities.FoodJokeEntity
import com.rajit.foodies.data.database.entities.RecipesEntity
import com.rajit.foodies.models.FoodJoke
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Query("SELECT * FROM RECIPES_TABLE ORDER BY id ASC")
    fun getRecipes(): Flow<List<RecipesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM favourite_recipes ORDER BY id ASC")
    fun getFavouriteRecipes(): Flow<List<FavouritesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavouriteRecipes(favouritesEntity: FavouritesEntity)

    @Delete
    suspend fun deleteFavouriteRecipes(favouritesEntity: FavouritesEntity)

    @Query("DELETE FROM favourite_recipes")
    suspend fun deleteAllFavourites()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM food_joke_table ORDER BY id ASC")
    fun getFoodJoke(): Flow<List<FoodJokeEntity>>

}
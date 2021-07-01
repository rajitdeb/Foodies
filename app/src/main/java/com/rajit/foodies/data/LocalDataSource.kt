package com.rajit.foodies.data

import com.rajit.foodies.data.database.RecipesDao
import com.rajit.foodies.data.database.entities.FavouritesEntity
import com.rajit.foodies.data.database.entities.FoodJokeEntity
import com.rajit.foodies.data.database.entities.RecipesEntity
import com.rajit.foodies.models.FoodJoke
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// repository for CRUD operations in ROOM
class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    /**
     * Recipes
     */

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.getRecipes()
    }

    /**
     * Favourite Recipes
     */

    suspend fun saveFavouriteRecipes(favouritesEntity: FavouritesEntity) {
        recipesDao.saveFavouriteRecipes(favouritesEntity)
    }

    suspend fun deleteFavouriteRecipes(favouritesEntity: FavouritesEntity) {
        recipesDao.deleteFavouriteRecipes(favouritesEntity)
    }

    suspend fun deleteAllFavourites() {
        recipesDao.deleteAllFavourites()
    }

    fun readFavourites(): Flow<List<FavouritesEntity>> {
        return recipesDao.getFavouriteRecipes()
    }

    /**
     * Food Joke
     */

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) {
        return recipesDao.insertFoodJoke(foodJokeEntity)
    }

    fun readFoodJoke(): Flow<List<FoodJokeEntity>> {
        return recipesDao.getFoodJoke()
    }

}
package com.rajit.foodies.data

import android.util.Log
import com.rajit.foodies.data.network.FoodRecipesApi
import com.rajit.foodies.models.FoodJoke
import com.rajit.foodies.models.FoodRecipes
import retrofit2.Response
import javax.inject.Inject

// repository for network operations
class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    // this is for getting recipes with meal and diet type
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipes> {
        return foodRecipesApi.getRecipes(queries)
    }

    // this is for searching recipes with a search query
    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipes> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    // this is for getting food joke (random)
    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
        val response = foodRecipesApi.getFoodJoke(apiKey)
//        Log.d("FoodJokeResponse", response.toString())
        return response
    }

}
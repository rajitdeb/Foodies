package com.rajit.foodies.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.rajit.foodies.data.Repository
import com.rajit.foodies.data.database.entities.FavouritesEntity
import com.rajit.foodies.data.database.entities.FoodJokeEntity
import com.rajit.foodies.data.database.entities.RecipesEntity
import com.rajit.foodies.models.FoodJoke
import com.rajit.foodies.models.FoodRecipes
import com.rajit.foodies.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE **/

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()

    val readFavouriteRecipes: LiveData<List<FavouritesEntity>> =
        repository.local.readFavourites().asLiveData()

    val readFoodJoke: LiveData<List<FoodJokeEntity>> =
        repository.local.readFoodJoke().asLiveData()

    fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun saveFaouriteRecipe(favouritesEntity: FavouritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.saveFavouriteRecipes(favouritesEntity)
        }

    fun deleteFavouriteRecipes(favouritesEntity: FavouritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavouriteRecipes(favouritesEntity)
        }

    fun deleteAllFavourites() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllFavourites()
    }

    private fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertFoodJoke(foodJokeEntity)
    }

    /** RETROFIT **/
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()

    var searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipes>> = MutableLiveData()

    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    // this function will be used by activity/fragment for getting recipes
    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }

    // handling network connection errors
    private suspend fun getFoodJokeSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {

                val response = repository.remote.getFoodJoke(apiKey)
                Log.d("FoodJokeResponse", response.body().toString())
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value!!.data
                if(foodJoke != null) {
                    offlineCacheFoodJoke(foodJoke)
                }

            } catch (e: Exception) {
                foodJokeResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            foodJokeResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // handling network response codes
    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke> {
        return when {
            response.message().contains("timeout") -> {
                NetworkResult.Error("Network Timeout")
            }

            response.code() == 402 -> {
                NetworkResult.Error("API Key Limitation")
            }

            response.isSuccessful -> {
                val foodJoke = response.body()
                NetworkResult.Success(foodJoke!!)
            }

            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    fun searchRecipesWithSearchQuery(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    // handling network connection errors
    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {

                val response = repository.remote.searchRecipes(searchQuery)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)

            } catch (e: Exception) {
                searchRecipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    // internet connection is handled here and response is stored in the recipesResponse variable
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {

        recipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            try {

                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipes = recipesResponse.value!!.data
                if (foodRecipes != null) {
                    offlineCacheRecipes(foodRecipes)
                }

            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }


    private fun offlineCacheRecipes(foodRecipes: FoodRecipes) {

        // Converting food recipes to recipes entity for offline caching
        val recipesEntity = RecipesEntity(foodRecipes)

        // This function is created in the viewmodel with coroutine scope
        insertRecipes(recipesEntity)

    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {

        // Converting food recipes to recipes entity for offline caching
        val foodJokeEntity = FoodJokeEntity(foodJoke)

        // This function is created in the viewmodel with coroutine scope
        insertFoodJoke(foodJokeEntity)

    }

    // All the error and success responses are handled here
    private fun handleFoodRecipesResponse(response: Response<FoodRecipes>): NetworkResult<FoodRecipes> {
        when {
            response.message().contains("timeout") -> {
                return NetworkResult.Error("Network Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limitation")
            }

            response.body()!!.results.isEmpty() -> {
                return NetworkResult.Error("Recipes not found")
            }

            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    // code for checking internet connectivity
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}
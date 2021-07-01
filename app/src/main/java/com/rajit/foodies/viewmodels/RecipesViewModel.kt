package com.rajit.foodies.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rajit.foodies.data.DataStoreRepository
import com.rajit.foodies.utils.Constants
import com.rajit.foodies.utils.Constants.Companion.API_KEY
import com.rajit.foodies.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.rajit.foodies.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.rajit.foodies.utils.Constants.Companion.NUMBER_OF_RESULTS
import com.rajit.foodies.utils.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.rajit.foodies.utils.Constants.Companion.QUERY_API_KEY
import com.rajit.foodies.utils.Constants.Companion.QUERY_DIET
import com.rajit.foodies.utils.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.rajit.foodies.utils.Constants.Companion.QUERY_NUMBER
import com.rajit.foodies.utils.Constants.Companion.QUERY_TYPE
import com.rajit.foodies.utils.Constants.Companion.SEARCH_QUERY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
/**
 * USE HiltViewModel instead of ViewModel Inject with regular @Inject
 * latter is deprecated and former is used in recent versions
 **/
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    // Initialized as default parameters but will contain data from Datastore
    var mealType = DEFAULT_MEAL_TYPE
    var dietType = DEFAULT_DIET_TYPE
    
    // stores network status (boolean) from [RecipesFragment::class]
    var networkStatus = false
    var backOnline = false

    // this retrieves stored meal and diet type from datastore preferences
    val readMealAndDietType = dataStoreRepository.readMealAndDietType()

    // persist network status
    val readBackOnline = dataStoreRepository.readBackOnline().asLiveData()

    // take data from fragment and use viewmodel to save meal and diet type to preferences
    // this approach is mainly used for mvvm architecture
    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    private fun saveBackOnline(backOnline: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveBackOnline(backOnline)
    }

    // this applies the queries to the api
    fun applyQueries(): HashMap<String, String> {

        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            // collect is used to read specific value from datastore repository
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = NUMBER_OF_RESULTS
        queries[QUERY_API_KEY] = API_KEY
        Log.d("RecipesViewModel", "applyQueries: mealType: $mealType, dietType: $dietType")
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {

        val queries: HashMap<String, String> = HashMap()

        queries[SEARCH_QUERY] = searchQuery
        queries[QUERY_NUMBER] = NUMBER_OF_RESULTS
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries

    }

    // displays a toast when internet is unavailable
    fun showNetworkStatus() {
        if(!networkStatus){
            Log.d("Network/BackOnline", "backOnline: $backOnline")
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if(networkStatus) {
            if(backOnline){
                Log.d("Network/BackOnline", "backOnline: $backOnline")
                Toast.makeText(getApplication(), "We're Back Online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
            Log.d("Network/BackOnline", "backOnline: $backOnline")
        }
    }

}
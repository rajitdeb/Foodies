package com.rajit.foodies.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.rajit.foodies.utils.Constants
import com.rajit.foodies.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.rajit.foodies.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.rajit.foodies.utils.Constants.Companion.PREFERENCES_NAME
import com.rajit.foodies.utils.Constants.Companion.SELECTED_DIET_TYPE
import com.rajit.foodies.utils.Constants.Companion.SELECTED_DIET_TYPE_ID
import com.rajit.foodies.utils.Constants.Companion.SELECTED_MEAL_TYPE
import com.rajit.foodies.utils.Constants.Companion.SELECTED_MEAL_TYPE_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // keys of preferences for easy access
    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(SELECTED_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(SELECTED_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(SELECTED_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(SELECTED_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey("backOnline")
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    // saving Meal and Diet preferences of user from bottomsheet to preferences
    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {

        dataStore.edit { preferences ->

            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId

        }

    }

    // saving network state
    suspend fun saveBackOnline(backOnline: Boolean) {

        dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }

    }

    // reading data from preferences
    fun readMealAndDietType(): Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    // reading network state and display data accordingly
    fun readBackOnline(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }
}

// model for retrieving data from preferences
data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)
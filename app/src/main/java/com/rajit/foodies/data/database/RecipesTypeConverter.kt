package com.rajit.foodies.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rajit.foodies.models.FoodRecipes
import com.rajit.foodies.models.Result

class RecipesTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun foodRecipesToString(foodRecipes: FoodRecipes) : String {
        return gson.toJson(foodRecipes)
    }

    @TypeConverter
    fun stringToFoodRecipes(data: String) : FoodRecipes {
        val listType = object: TypeToken<FoodRecipes>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resultToString(result: Result) : String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String) : Result {
        val lisType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, lisType)
    }

}
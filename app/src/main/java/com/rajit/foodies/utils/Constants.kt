package com.rajit.foodies.utils

class Constants {

    companion object {

        const val BASE_URL = "https://api.spoonacular.com/"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"

        const val API_KEY = "YOUR_API_KEY"

        const val RECIPE_RESULT = "recipeBundle"

        // API Query Keys
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS= "fillIngredients"

        // ROOM Database
        const val DATABASE_NAME = "recipes_database"
        const val TABLE_NAME = "recipes_table"

        const val FAVOURITE_TABLE_NAME = "favourite_recipes"

        const val FOOD_JOKE_TABLE_NAME = "food_joke_table"

        // Bottom Sheet and Preferences
        const val NUMBER_OF_RESULTS = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFERENCES_NAME = "mealAndDietType"

        const val SELECTED_MEAL_TYPE = "selectedMealType"
        const val SELECTED_MEAL_TYPE_ID = "selectedMealTypeId"
        const val SELECTED_DIET_TYPE = "selectedDietType"
        const val SELECTED_DIET_TYPE_ID = "selectedDietTypeId"

        // Search Queries
        const val SEARCH_QUERY = "query"

    }

}
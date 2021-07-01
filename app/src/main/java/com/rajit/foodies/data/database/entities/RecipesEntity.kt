package com.rajit.foodies.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rajit.foodies.models.FoodRecipes
import com.rajit.foodies.utils.Constants.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class RecipesEntity(
    var foodRecipes: FoodRecipes
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}
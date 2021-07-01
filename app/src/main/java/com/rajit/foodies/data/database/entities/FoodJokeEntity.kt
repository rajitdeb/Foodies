package com.rajit.foodies.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rajit.foodies.models.FoodJoke
import com.rajit.foodies.utils.Constants.Companion.FOOD_JOKE_TABLE_NAME

@Entity(tableName = FOOD_JOKE_TABLE_NAME)
data class FoodJokeEntity(
    @Embedded
    val foodJoke: FoodJoke
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}
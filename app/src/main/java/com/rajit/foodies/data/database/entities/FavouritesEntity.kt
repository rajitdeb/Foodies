package com.rajit.foodies.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rajit.foodies.models.Result
import com.rajit.foodies.utils.Constants.Companion.FAVOURITE_TABLE_NAME

@Entity(tableName = FAVOURITE_TABLE_NAME)
class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var resultRecipe: Result
)
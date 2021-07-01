package com.rajit.foodies.models

import com.google.gson.annotations.SerializedName

data class FoodJoke(
    @SerializedName("text")
    val joke: String
)

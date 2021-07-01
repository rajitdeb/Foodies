package com.rajit.foodies.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.rajit.foodies.data.database.entities.RecipesEntity
import com.rajit.foodies.models.FoodRecipes
import com.rajit.foodies.utils.NetworkResult

class RecipesBinding {

    companion object {

        /**
         * This binding adapter is for setting the visibility of Network Error Views(Imageview, TextView)
         * on Network Availability
         **/
        @BindingAdapter("android:readApiResponse", "android:readDatabase", requireAll = true)
        @JvmStatic
        fun handleReadDataErrors(
            view: View,
            apiResponse: NetworkResult<FoodRecipes>?,
            database: List<RecipesEntity>?
        ) {
            when(view) {
                is ImageView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                }
                is TextView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    view.text = apiResponse?.message.toString()
                }
            }
        }


    }

}
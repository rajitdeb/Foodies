package com.rajit.foodies.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import com.rajit.foodies.data.database.entities.FoodJokeEntity
import com.rajit.foodies.models.FoodJoke
import com.rajit.foodies.utils.NetworkResult

class FoodJokeBinding {

    companion object {

        /**
         * This binding adapter is for setting the visibility of CardView & Progressbar
         * based on response received from the network
         **/
        @BindingAdapter(
            "android:readApiFoodJoke",
            "android:readDatabaseFoodJoke",
            requireAll = false
        )
        @JvmStatic
        fun setCardAndProgressVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>,
            databaseResponse: List<FoodJokeEntity>?
        ) {

            when (apiResponse) {

                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.VISIBLE
                        is MaterialCardView -> view.visibility = View.INVISIBLE
                    }
                }

                is NetworkResult.Success -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is MaterialCardView -> view.visibility = View.VISIBLE
                    }
                }

                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE

                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                            if (databaseResponse != null) {
                                if (databaseResponse.isEmpty()) {
                                    view.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }

            }

        }

        /**
         * This binding adapter is for setting the visibility of Network Error Views(Imageview, TextView)
         * on Network Availability
         **/
        @BindingAdapter(
            "android:readApiForError",
            "android:readDatabaseForError",
            requireAll = true
        )
        @JvmStatic
        fun setErrorViewsVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            databaseResponse: List<FoodJokeEntity>?
        ) {

            if(databaseResponse != null) {
                if(databaseResponse.isEmpty()) {
                    view.visibility = View.VISIBLE
                    if(view is TextView) {
                        if(apiResponse != null) {
                            view.text = apiResponse.message.toString()
                        }
                    }
                }
            }

            if (apiResponse is NetworkResult.Success || apiResponse is NetworkResult.Loading) {
                view.visibility = View.INVISIBLE
            }

        }

    }

}
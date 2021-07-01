package com.rajit.foodies.bindingadapters

import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rajit.foodies.adapters.FavouriteRecipesAdapter
import com.rajit.foodies.data.database.entities.FavouritesEntity

class FavouriteRecipesBinding {

    companion object {

        /**
         * This binding adapter is for toggling the visibility of Network Error Views(Imageview, TextView)
         * and RecyclerView based on availability of data in database
         *
         **/
        @BindingAdapter("android:setVisibility", "android:setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favouritesEntity: List<FavouritesEntity>?,
            mAdapter: FavouriteRecipesAdapter?
        ) {
            when (view) {

                is RecyclerView -> {
                    val dataCheck = favouritesEntity.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    if (!dataCheck) {
                        favouritesEntity?.let {
                            mAdapter?.setData(it)
                        }
                    }
                }

                else -> {
                    view.isVisible = favouritesEntity.isNullOrEmpty()
                    Log.d("FavouritesBinding", "favouritesEntity: ${favouritesEntity.isNullOrEmpty()}")
                }

            }
        }

    }

}
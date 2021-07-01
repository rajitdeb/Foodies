package com.rajit.foodies.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.bumptech.glide.Glide
import com.rajit.foodies.R
import com.rajit.foodies.models.Result
import com.rajit.foodies.ui.fragments.recipes.RecipesFragmentDirections
import org.jsoup.Jsoup
import java.lang.Exception

class RecipesRowBindingAdapter {

    companion object {

        // Handles click and navigates user to the Details Activity
        @BindingAdapter("android:onRecipesClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipesRowLayout: ConstraintLayout, result: Result) {
            recipesRowLayout.setOnClickListener {
                try {
                    val action =
                        RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    recipesRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("RecipesBindingAdapter", e.message.toString())
                }
            }
        }

        @BindingAdapter("android:loadImage")
        @JvmStatic
        fun loadImage(imageView: ImageView, imageUrl: String) {
//            Glide.with(imageView)
//                .load(imageUrl)
//                .error(R.drawable.ic_error_placeholder)
//                .into(imageView)

            // Using COIL for image loading
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        // Toggling between colors of the views
        @BindingAdapter("android:isVegan")
        @JvmStatic
        fun isVegan(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {

                    is TextView -> view.setTextColor(
                        ContextCompat.getColor(view.context, R.color.green)
                    )

                    is ImageView -> view.setColorFilter(
                        ContextCompat.getColor(view.context, R.color.green)
                    )

                }
            }
        }

        // parsing all the HTML tags from the description text and displaying it normally
        @BindingAdapter("android:parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, description: String?) {
            if(description != null) {
                val parsedTxt = Jsoup.parse(description).text()
                textView.text = parsedTxt

            }
        }

    }

}
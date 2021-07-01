package com.rajit.foodies.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rajit.foodies.R
import com.rajit.foodies.databinding.IngredientsRowLayoutBinding
import com.rajit.foodies.models.ExtendedIngredient
import com.rajit.foodies.utils.Constants.Companion.BASE_IMAGE_URL
import com.rajit.foodies.utils.RecipesDiffUtil

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var extendedIngredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredients: ExtendedIngredient) {

            binding.ingredientName.text = ingredients.name
            binding.ingredientConsistency.text = ingredients.consistency
            binding.ingredientOriginal.text = ingredients.original
            binding.ingredientsAmount.text = ingredients.amount.toString()
            binding.ingredientsUnit.text = ingredients.unit

            Log.d("IngredientsAdapter", "Image Url: ${BASE_IMAGE_URL + ingredients.image}")

            // for monitoring the performance i used glide and coil in different places in the project
            Glide.with(binding.ingredientsIv)
                .load(BASE_IMAGE_URL + ingredients.image)
                .error(R.drawable.ic_error_placeholder)
                .into(binding.ingredientsIv)
        }

        // This step is just to keep the onCreateViewHolder fn clean
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val ingredientsRowLayoutBinding =
                    IngredientsRowLayoutBinding.inflate(inflater, parent, false)
                return MyViewHolder(ingredientsRowLayoutBinding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentIngredient = extendedIngredientsList[position]
        holder.bind(currentIngredient)
    }

    override fun getItemCount(): Int {
        return extendedIngredientsList.size
    }

    fun setData(newData: List<ExtendedIngredient>) {
        val ingredientsDiffUtil = RecipesDiffUtil(extendedIngredientsList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        extendedIngredientsList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}
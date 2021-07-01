package com.rajit.foodies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rajit.foodies.databinding.RecipesRowLayoutBinding
import com.rajit.foodies.models.FoodRecipes
import com.rajit.foodies.models.Result
import com.rajit.foodies.utils.RecipesDiffUtil

class RecipesAdapter() : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipesList = emptyList<Result>()

    class MyViewHolder(private val binding: RecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            // This binding.result is from the recipes binding layout
            binding.result = result
            binding.executePendingBindings()
        }

        // This step is just to keep the onCreateViewHolder fn clean
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val recipesRowLayoutBinding =
                    RecipesRowLayoutBinding.inflate(inflater, parent, false)
                return MyViewHolder(recipesRowLayoutBinding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipesList[position]
        holder.bind(currentRecipe)
    }

    override fun getItemCount(): Int {
        return recipesList.size
    }

    fun setData(newData: FoodRecipes) {
        val recipesDiffUtil = RecipesDiffUtil(recipesList, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipesList = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }

}
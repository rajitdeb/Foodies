package com.rajit.foodies.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rajit.foodies.R
import com.rajit.foodies.data.database.entities.FavouritesEntity
import com.rajit.foodies.databinding.FavouritesRowLayoutBinding
import com.rajit.foodies.ui.fragments.favourites.FavoritesFragmentDirections
import com.rajit.foodies.utils.RecipesDiffUtil
import com.rajit.foodies.viewmodels.MainViewModel

class FavouriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavouriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var favouriteRecipesList = emptyList<FavouritesEntity>()

    private var multiSelection = false

    private var selectedRecipesList = arrayListOf<FavouritesEntity>()

    private var myViewHolders = arrayListOf<MyViewHolder>()

    private lateinit var mActionMode: ActionMode

    private lateinit var rootView: View

    class MyViewHolder(val binding: FavouritesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favouriteRecipe: FavouritesEntity) {
            binding.favouritesEntity = favouriteRecipe
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val layoutBinding = FavouritesRowLayoutBinding.inflate(
                    inflater, parent, false
                )
                return MyViewHolder(layoutBinding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        myViewHolders.add(holder)
        rootView = holder.binding.root

        val currentFavourite = favouriteRecipesList[position]
        holder.bind(currentFavourite)

        saveItemOnScroll(currentFavourite, holder)

        /**
         * Single-Click Listener
         **/
        holder.binding.favouriteRecipesRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentFavourite)
            } else {
                val action =
                    FavoritesFragmentDirections
                        .actionFavoritesFragmentToDetailsActivity(currentFavourite.resultRecipe)
                holder.binding.root.findNavController().navigate(action)
            }
        }

        /**
         * Long-Click Listener
         **/
        holder.binding.favouriteRecipesRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                // This is for activating contextual mode
                requireActivity.startActionMode(this)
                applySelection(holder, currentFavourite)
                true

            } else {
                applySelection(holder, currentFavourite)
                true
            }

        }
    }

    override fun getItemCount(): Int {
        return favouriteRecipesList.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        // creation of contextual mode
        // inflating the favourites contextual mode
        actionMode?.menuInflater?.inflate(R.menu.favourites_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if(menu?.itemId == R.id.deleteFavourite) {
            selectedRecipesList.forEach {
                mainViewModel.deleteFavouriteRecipes(it)
            }
            showSnackBar("${selectedRecipesList.size} Recipe/s Deleted")
            multiSelection = false
            selectedRecipesList.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipesList.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    fun setData(newFavouriteList: List<FavouritesEntity>) {
        val recipesDiffUtil = RecipesDiffUtil(favouriteRecipesList, newFavouriteList)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        favouriteRecipesList = newFavouriteList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color)
    }

    private fun saveItemOnScroll(currentRecipe: FavouritesEntity, holder: MyViewHolder) {
        if(selectedRecipesList.contains(currentRecipe)) {
            changeRecipeStyle(
                holder,
                R.color.contextualCardBackgroundColor,
                R.color.contextualStrokeColor
            )
        } else {
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavouritesEntity) {
        if (selectedRecipesList.contains(currentRecipe)) {
            selectedRecipesList.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipesList.add(currentRecipe)
            changeRecipeStyle(
                holder,
                R.color.contextualCardBackgroundColor,
                R.color.contextualStrokeColor
            )
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(
        holder: MyViewHolder,
        backgroundColor: Int,
        strokeColor: Int
    ) {
//        holder.binding.favouriteRecipesRowLayout
//            .setBackgroundColor(ContextCompat.getColor(requireActivity, backgroundColor))
        holder.binding.favouriteRowCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when (selectedRecipesList.size) {
            0 -> {
                mActionMode.finish()
                multiSelection = false
            }
            1 -> {
                mActionMode.title = "${selectedRecipesList.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipesList.size} items selected"
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
            .setAction("OKAY") {}
            .show()
    }

    // This function exits the action mode for selection / deletion of recipes
    fun clearContextualActionMode() {
        if(this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

}
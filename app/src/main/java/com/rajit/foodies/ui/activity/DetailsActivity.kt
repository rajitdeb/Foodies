package com.rajit.foodies.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.rajit.foodies.R
import com.rajit.foodies.adapters.PagerAdapter
import com.rajit.foodies.data.database.entities.FavouritesEntity
import com.rajit.foodies.databinding.ActivityDetailsBinding
import com.rajit.foodies.ui.fragments.ingredients.IngredientsFragment
import com.rajit.foodies.ui.fragments.instructions.InstructionsFragment
import com.rajit.foodies.ui.fragments.overview.OverviewFragment
import com.rajit.foodies.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var binding: ActivityDetailsBinding

    private var savedRecipeID = 0
    private var isRecipeSaved = false

    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        binding.toolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Adding fragments to list
        val fragments = ArrayList<androidx.fragment.app.Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        // Adding titles to titles list
        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        // the recipes that is clicked on is passed here
        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle", args.result)

        val pagerAdapter = PagerAdapter(
            resultBundle,
            fragments,
            this
        )

        binding.viewPager2.apply {
            adapter = pagerAdapter
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == android.R.id.home) {
            finish()
        } else if (itemId == R.id.saveToFavourite && !isRecipeSaved) {
            saveFavourites(item)
        } else if (itemId == R.id.saveToFavourite && isRecipeSaved) {
            removeFromFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun removeFromFavourites(item: MenuItem) {
        val favouritesEntity = FavouritesEntity(
            savedRecipeID,
            args.result
        )
        mainViewModel.deleteFavouriteRecipes(favouritesEntity)
        changeItemColor(item, R.color.white)
        showSnackBar("Removed from favourites")
        isRecipeSaved = false
    }

    private fun saveFavourites(item: MenuItem) {
        val favouritesEntity = FavouritesEntity(
            0,
            args.result
        )
        mainViewModel.saveFaouriteRecipe(favouritesEntity)
        changeItemColor(item, R.color.yellow)
        showSnackBar("Recipe Saved Successfully")
        isRecipeSaved = true
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setAction("OKAY") {}
            .show()
    }

    private fun changeItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.saveToFavourite)
        checkSavedRecipe(menuItem)
        return true
    }

    private fun checkSavedRecipe(item: MenuItem) {
        mainViewModel.readFavouriteRecipes.observe(this) { favouriteRecipes ->
            for (savedRecipe in favouriteRecipes) {
                try {
                    if (savedRecipe.resultRecipe.id == args.result.id) {
                        isRecipeSaved = true
                        savedRecipeID = savedRecipe.id
                        changeItemColor(item, R.color.yellow)
                    }
                } catch (e: Exception) {
                    Log.d("DetailsActivity", e.message.toString())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        changeItemColor(menuItem, R.color.white)
    }
}
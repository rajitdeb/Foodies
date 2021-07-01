package com.rajit.foodies.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajit.foodies.R
import com.rajit.foodies.adapters.RecipesAdapter
import com.rajit.foodies.databinding.FragmentRecipiesBinding
import com.rajit.foodies.utils.NetworkListener
import com.rajit.foodies.utils.NetworkResult
import com.rajit.foodies.utils.observeOnce
import com.rajit.foodies.viewmodels.MainViewModel
import com.rajit.foodies.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var networkListener: NetworkListener

    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding: FragmentRecipiesBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by viewModels<MainViewModel>()
    private val recipesViewModel by viewModels<RecipesViewModel>()
    private val mAdapter: RecipesAdapter by lazy { RecipesAdapter() }

    private var searchView: SearchView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipiesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.myMainViewmodel = mainViewModel

        setHasOptionsMenu(true)

        setupRecyclerView()

        // checking network state
        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("RecipesFragment/NetworkListener", "Status: $status")
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModel.backOnline = it
        })

        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            showShimmerEffect()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu, menu)

        val search = menu.findItem(R.id.search_recipes)
        searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    // Searching for a recipe: Pressed Search btn
    override fun onQueryTextSubmit(searchQuery: String?): Boolean {
        if (searchQuery != null) {
            searchRecipesWithQuery(searchQuery)
            searchView?.clearFocus()
        }
        return true
    }

    // handling the search response
    private fun searchRecipesWithQuery(searchQuery: String) {
        showShimmerEffect()

        mainViewModel.searchRecipesWithSearchQuery(
            recipesViewModel.applySearchQuery(searchQuery)
        )

        mainViewModel.searchRecipesResponse.observe(viewLifecycleOwner) { result ->

            when (result) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    result.data?.let {
                        mAdapter.setData(it)
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        result.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    readFromDatabaseOnError()
                }

                is NetworkResult.Loading -> showShimmerEffect()
            }

        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }


    // This function is called for reading from database which is a single source of truth
    private fun readDatabase() {
        lifecycleScope.launch {
            // This observeOnce is a custom extension function and is used to restrict this code
            // to be called only once
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("Recipes Fragment", "readDatabase called ")
                    mAdapter.setData(database[0].foodRecipes)
                    hideShimmerEffect()
                } else {
                    requestDataFromApi()
                }
            })
        }
    }

    /**
     * Request data from api and passes data to adapter
     * Also handles loading, success, error states
     * Showing and hiding of Shimmer Effect
     **/
    private fun requestDataFromApi() {
        Log.d("Recipes Fragment", "requestDataFromApi called ")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it)}
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    readFromDatabaseOnError()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    /** CALLED IN CASE OF NETWORK ERROR
     *
     * This function is called when there is a network error
     * It populates data from database
     * If there is not data in the database the recycler view will be empty
     *
     **/
    private fun readFromDatabaseOnError() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipes)
                }
            }
        }
    }


    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.shimmerFrameLayout.startShimmer()
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.recyclerView.visibility = View.VISIBLE
        binding.shimmerFrameLayout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
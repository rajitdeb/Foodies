package com.rajit.foodies.ui.fragments.favourites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rajit.foodies.R
import com.rajit.foodies.adapters.FavouriteRecipesAdapter
import com.rajit.foodies.databinding.FragmentFavoritesBinding
import com.rajit.foodies.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private val mAdapter: FavouriteRecipesAdapter by lazy {
        FavouriteRecipesAdapter(requireActivity(), mainViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        setHasOptionsMenu(true)

        binding.favouritesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favourites_delete_all_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAllFavourites) {
            if(mainViewModel.readFavouriteRecipes.value!!.isNotEmpty()){
                mainViewModel.deleteAllFavourites()
                showSnackBar("All recipes removed")
            } else{
                showSnackBar("Nothing to delete")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setAction("OKAY") {}
            .show()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.clearContextualActionMode()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
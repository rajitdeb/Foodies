package com.rajit.foodies.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajit.foodies.adapters.IngredientsAdapter
import com.rajit.foodies.databinding.FragmentIngredientsBinding
import com.rajit.foodies.models.Result
import com.rajit.foodies.utils.Constants.Companion.RECIPE_RESULT

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(layoutInflater, container, false)

        // fetching the passed on recipe in arguments
        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT)
        val ingredientsList = myBundle?.extendedIngredients

        binding.ingredientsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            ingredientsList?.let { mAdapter.setData(it) }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.rajit.foodies.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.bumptech.glide.Glide
import com.rajit.foodies.R
import com.rajit.foodies.bindingadapters.RecipesRowBindingAdapter
import com.rajit.foodies.databinding.FragmentOverviewBinding
import com.rajit.foodies.models.Result
import com.rajit.foodies.utils.Constants.Companion.RECIPE_RESULT

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result = args!!.getParcelable<Result>(RECIPE_RESULT) as Result

        // using COIL for image loading
        binding.recipeImageBackdrop.load(myBundle.image) {
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }

        binding.likesTvDetails.text = myBundle.aggregateLikes.toString()

        binding.cookingTimeTvDetails.text = myBundle.readyInMinutes.toString()

        binding.recipeTitleDetails.text = myBundle.title

        RecipesRowBindingAdapter.parseHtml(binding.descriptionTvDetails, myBundle.summary)

        // updates chips in overview fragment
        updateTextViewAndImageViewColor(
            myBundle.vegetarian,
            binding.vegetarianTv,
            binding.vegetarianTick
        )

        updateTextViewAndImageViewColor(myBundle.vegan, binding.veganTv, binding.veganTick)

        updateTextViewAndImageViewColor(
            myBundle.glutenFree,
            binding.glutenFreeTv,
            binding.glutenFreeTick
        )

        updateTextViewAndImageViewColor(
            myBundle.dairyFree,
            binding.dairyFreeTv,
            binding.dairyFreeTick
        )

        updateTextViewAndImageViewColor(
            myBundle.veryHealthy,
            binding.healthyTv,
            binding.healthyTick
        )

        updateTextViewAndImageViewColor(myBundle.cheap, binding.cheapTv, binding.cheapTick)


        return binding.root
    }

    private fun updateTextViewAndImageViewColor(
        stateIsOn: Boolean,
        textView: TextView,
        imageView: ImageView
    ) {
        if (stateIsOn) {
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
            imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
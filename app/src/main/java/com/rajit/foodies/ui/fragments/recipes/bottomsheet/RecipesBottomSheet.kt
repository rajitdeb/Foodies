package com.rajit.foodies.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.rajit.foodies.databinding.RecipesBottomSheetBinding
import com.rajit.foodies.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.rajit.foodies.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.rajit.foodies.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * This is a bottom sheet fragment used in [RecipesFragment::class].
 **/

private const val TAG = "RecipesBottomSheet"

@AndroidEntryPoint
class RecipesBottomSheet : BottomSheetDialogFragment() {

    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val recipesViewModel by viewModels<RecipesViewModel>()

    private var mealType: String = DEFAULT_MEAL_TYPE
    private var mealTypeId: Int = 0
    private var dietType: String = DEFAULT_DIET_TYPE
    private var dietTypeId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)

        // reading data from preferences (Datastore)
        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
            mealType = value.selectedMealType
            dietType = value.selectedDietType
            updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
        }

        // getting the values from selected chips (Meal Type)
        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealType = selectedMealType
            mealTypeId = selectedChipId
        }

        // getting the values from selected chips (Diet Type)
        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietType = selectedDietType
            dietTypeId = selectedChipId
        }

        binding.applyBtn.setOnClickListener {
//            Toast.makeText(
//                requireContext(),
//                "mealType: $mealType, dietType: $dietType",
//                Toast.LENGTH_SHORT
//            ).show()
            recipesViewModel.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
            val action =
                RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d(TAG, "Error occurred: ${e.message.toString()}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
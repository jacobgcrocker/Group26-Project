package com.example.wat2eat.ui.recipe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wat2eat.databinding.FragmentRecipeBinding
import com.example.wat2eat.models.DetailedRecipe
import com.example.wat2eat.models.Ingredient
import com.example.wat2eat.models.Step
import com.example.wat2eat.ui.reviews.ReviewActivity
import com.squareup.picasso.Picasso
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeViewModel: RecipeViewModel
    
    private var ingredientsText: String = ""
    private var instructionsText: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeViewModel = ViewModelProvider(this).get(RecipeViewModel::class.java)
        binding.seeReviews.setOnClickListener {
            val intent = Intent(context, ReviewActivity::class.java)
            startActivity(intent)
        }

        binding.instructionsTextView.setLineSpacing(8.0f, 1.1f)

        binding.copyIngredientsButton.setOnClickListener {
            val clipboard = requireContext().getSystemService(ClipboardManager::class.java)
            val clip = ClipData.newPlainText("Ingredients", ingredientsText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied ingredients to clipboard", Toast.LENGTH_SHORT).show()
        }

        binding.copyInstructionsButton.setOnClickListener {
            val clipboard = requireContext().getSystemService(ClipboardManager::class.java)
            val clip = ClipData.newPlainText("Instructions", instructionsText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied instructions to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateRecipeDetails(recipe: DetailedRecipe) {
        Picasso.get()
            .load(recipe.image)
            .fit()
            .centerCrop()
            .into(binding.recipeImageView)
        binding.recipeTitle.text = recipe.title
        binding.cookingTime.text = buildString {
            append(recipe.readyInMinutes)
            append(" minutes")
        }
        // TODO: get calories
        binding.servings.text = buildString {
            append(recipe.servings)
            append(" serving(s)")
        }

        updateIngredients(recipe.extendedIngredients)
        if (recipe.analyzedInstructions.isNotEmpty()) {
            updateInstructions(recipe.analyzedInstructions[0].steps)
        } else {
            binding.instructionsTextView.text = ""
        }
    }

    private fun updateIngredients(ingredients: List<Ingredient>) {
        binding.ingredientsContainer.removeAllViews()

        var formattedIngredients = ""
        for (ingredient in ingredients) {
            val checkBox = CheckBox(requireContext())
            checkBox.apply {
                text = buildString {
                    append("${ingredient.name}: ${ingredient.amount} ${ingredient.unit}")
                    formattedIngredients += "${ingredient.name}: ${ingredient.amount} ${ingredient.unit}\n"
                }
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                textSize = 15f
            }
            binding.ingredientsContainer.addView(checkBox)
            ingredientsText = formattedIngredients
        }
    }

    private fun updateInstructions(steps: List<Step>) {
        val formattedInstructions = buildString {
            for ((index, step) in steps.withIndex()) {
                append("${index + 1}. ${step.step}\n\n")
            }
        }
        binding.instructionsTextView.text = formattedInstructions
        instructionsText = formattedInstructions
    }
}
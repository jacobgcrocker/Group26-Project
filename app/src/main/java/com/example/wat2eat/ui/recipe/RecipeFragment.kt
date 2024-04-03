package com.example.wat2eat.ui.recipe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wat2eat.R
import com.example.wat2eat.api.RetrofitClient
import com.example.wat2eat.databinding.FragmentRecipeBinding
import com.example.wat2eat.models.DetailedRecipe
import com.example.wat2eat.models.Ingredient
import com.example.wat2eat.models.Step
import com.example.wat2eat.ui.reviews.ReviewActivity
import com.squareup.picasso.Picasso

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.content.ClipData
import android.content.ClipboardManager
import kotlin.math.roundToInt


class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private var userId: String = ""
    private var recipeId: String = ""
    private var isFavourite: Boolean = false

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
        recipeViewModel = ViewModelProvider(this)[RecipeViewModel::class.java]
        binding.seeReviews.setOnClickListener {
            val intent = Intent(context, ReviewActivity::class.java).apply {
                putExtra(ReviewActivity.EXTRA_RECIPE_ID, recipeId.toInt())
            }
            startActivity(intent)
        }

        binding.instructionsTextView.setLineSpacing(8.0f, 1.1f)

        userId = requireArguments().getString("userId")!!
        recipeId = requireArguments().getString("recipeId")!!

        getFavouriteStatus()

        binding.recipeImageButtonBack.setOnClickListener {
            requireActivity().finish()
        }
        binding.recipeImageButtonFavourite.setOnClickListener {
            toggleFavouriteStatus()
        }

        binding.copyIngredientsButton.setOnClickListener {
            val clipboard = requireContext().getSystemService(ClipboardManager::class.java)
            val clip = ClipData.newPlainText("Ingredients", ingredientsText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Copied ingredients to clipboard", Toast.LENGTH_SHORT).show()
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

        val caloriesPerServing = recipe.nutrition.nutrients.find { it.name == "Calories" }
        // calories is per serving so need to multiply by servings
        val calories = caloriesPerServing!!.amount.times(recipe.servings).roundToInt()
        binding.calories.text = buildString {
            append(calories)
            append(" ${caloriesPerServing.unit}")
        }

        binding.servings.text = buildString {
            append(recipe.servings)
            append(" serving(s)")
        }

        updateIngredients(recipe.extendedIngredients)
        if (recipe.analyzedInstructions.isNotEmpty()) {
            updateEquipment(recipe.analyzedInstructions[0].steps)
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

    private fun updateEquipment(instructionSteps: List<Step>) {
        val equipmentSet = mutableSetOf<String>()
        instructionSteps.forEach { step ->
            step.equipment.forEach { equipmentSet.add(it.name) }
        }
        binding.equipmentContainer.removeAllViews()
        for (eq in equipmentSet) {
            val checkBox = CheckBox(requireContext())
            checkBox.apply {
                text = buildString {
                    append(eq)
                }
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                textSize = 15f
            }
            binding.equipmentContainer.addView(checkBox)
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

    private fun updateFavouriteStatus(favourite: Boolean) {
        if (favourite) {
            binding.recipeImageButtonFavourite.setImageResource(R.drawable.ic_favorite_24)
        } else {
            binding.recipeImageButtonFavourite.setImageResource(R.drawable.ic_favorite_border_24)
        }
    }

    private fun getFavouriteStatus() {
        val call = RetrofitClient.userServiceInstance.isFavourite(userId, recipeId)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (res == null) {
                    Toast.makeText(
                        requireActivity(),
                        "Error fetching favourite status!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    isFavourite = res
                    updateFavouriteStatus(res)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                print(t)
            }
        })
    }

    private fun toggleFavouriteStatus() {
        val body = HashMap<String, String>()
        body["userId"] = userId
        body["recipeId"] = recipeId
        body["favourite"] = (!isFavourite).toString()
        val call = RetrofitClient.userServiceInstance.toggleFavourite(body)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (res == null) {
                    Toast.makeText(
                        requireActivity(),
                        "Error toggling favourite status!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    updateFavouriteStatus(res)
                    isFavourite = res
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                print(t)
            }
        })
    }
}
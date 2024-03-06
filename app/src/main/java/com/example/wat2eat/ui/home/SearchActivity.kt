package com.example.wat2eat.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wat2eat.R
import com.example.wat2eat.databinding.ActivitySearchBinding
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.wat2eat.api.RecipeResponse
import com.example.wat2eat.api.RetrofitClient
import com.google.android.material.chip.Chip
import android.widget.Toast
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wat2eat.ui.recipe.RecipeActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private lateinit var loading: android.widget.ProgressBar

    private lateinit var searchFilters: ScrollView
    private lateinit var searchBar: android.widget.SearchView
    private lateinit var minuteChipGroup: com.google.android.material.chip.ChipGroup
    private lateinit var caloriesSlider: com.google.android.material.slider.RangeSlider

    private lateinit var searchResults: ScrollView
    private lateinit var recipes : RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        loading = binding.loading

        searchFilters = binding.searchFilters
        searchBar = binding.searchBar
        minuteChipGroup = binding.minuteChipGroup
        caloriesSlider = binding.caloriesSlider

        searchResults = binding.searchResults
        recipes = RecipeAdapter() {
            val intent = Intent(this@SearchActivity, RecipeActivity::class.java)
            // TODO: replace with actual recipe id
            val extras = Bundle().apply {
                putString("EXTRA_USERNAME", "my_username")
                putString("EXTRA_PASSWORD", "my_password")
            }
            intent.putExtras(extras)
            startActivity(Intent(intent))
        }
        binding.rvSearchResults.apply {
            adapter = recipes
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }

        // TODO: make separate function

        searchBar.setOnQueryTextListener(object: android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val minuteChipId = minuteChipGroup.checkedChipId
                    val minuteChipText = minuteChipGroup.findViewById<Chip>(minuteChipId)?.text
                    val minutesString = minuteChipText?.toString()?.split(" ")?.get(0)
                    val minutes = minutesString?.toIntOrNull()

                    val calories = caloriesSlider.values
                    val minCalories = calories[0].toInt()
                    val maxCalories = calories[1].toInt()

                    binding.root.clearFocus()
                    searchFilters.visibility = android.view.View.GONE
                    searchResults.visibility = android.view.View.GONE
                    loading.visibility = android.view.View.VISIBLE
                    searchRecipes(query, minutes, minCalories, maxCalories)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        setContentView(binding.root)
    }

    private fun searchRecipes(query: String, minutes: Int? = null, minCalories: Int? = null, maxCalories: Int? = null) {
        val call = RetrofitClient.instance.searchRecipes(q = query, maxReadyTime = minutes, minCalories = minCalories, maxCalories = maxCalories)
        call.enqueue(object: Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                val responses = response.body()?.results
                Log.i("Recipes Response:", responses.toString())
                loading.visibility = android.view.View.GONE
                if (responses == null || responses.isEmpty()) {
                    searchFilters.visibility = android.view.View.VISIBLE
                    Toast.makeText(this@SearchActivity, "Error fetching recipes!", Toast.LENGTH_LONG).show()
                } else {
                    recipes.submitList(responses)
                    searchResults.visibility = android.view.View.VISIBLE
                    binding.rvSearchResults.clearFocus()
                }
            }
            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                print(t)
            }
        })
    }
}

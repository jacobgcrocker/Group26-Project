package com.example.wat2eat.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wat2eat.R
import com.example.wat2eat.ui.prefs.PrefPopup
import com.example.wat2eat.ui.prefs.PreferencesViewModel
import com.example.wat2eat.ui.recipe.RecipeActivity
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private lateinit var loading: android.widget.ProgressBar

    private lateinit var filterButton: android.widget.ImageButton

    private lateinit var searchFilters: ScrollView
    private lateinit var searchBar: android.widget.SearchView
    private lateinit var mealTypeChipGroup: com.google.android.material.chip.ChipGroup
    private lateinit var minuteChipGroup: com.google.android.material.chip.ChipGroup
    private lateinit var caloriesSlider: com.google.android.material.slider.RangeSlider
    private lateinit var healthButton: android.widget.Button
    private lateinit var cuisineDialog: com.example.wat2eat.widgets.MultiSelectDialog


    private lateinit var searchResults: ScrollView
    private lateinit var recipes : RecipeAdapter
    private val prefViewModel by viewModels<PreferencesViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        loading = binding.loading

        filterButton = binding.filterButton

        searchFilters = binding.searchFilters
        searchBar = binding.searchBar
        mealTypeChipGroup = binding.mealTypeChipGroup
        minuteChipGroup = binding.minuteChipGroup
        caloriesSlider = binding.caloriesSlider
        healthButton = binding.healthButton
        cuisineDialog = binding.cuisineDialog


        searchResults = binding.searchResults
        recipes = RecipeAdapter() {
            val intent = Intent(this@SearchActivity, RecipeActivity::class.java)
            val extras = Bundle().apply {
                putString("recipe_id", it.id.toString())
            }
            intent.putExtras(extras)
            startActivity(Intent(intent))
        }
        binding.rvSearchResults.apply {
            adapter = recipes
            layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        }

        val prefViewModel: PreferencesViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                prefViewModel.uiState.collect { state ->
                    binding.prefsCompose.apply {
                        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                        setContent {
                            val coroutineScope = rememberCoroutineScope()
                            val visible: Boolean = state.isBottomSheetOpen

                            val sheetState = rememberModalBottomSheetState(
                                skipPartiallyExpanded = true,
                                )
                            val hideSheet: () -> Unit = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    prefViewModel.toggleBottomSheet()
                                    prefViewModel.updatePrefs()
                                }
                            }
                            PrefPopup(
                                visible = visible,
                                state = state,
                                sheetState = sheetState,
                                onDismiss = hideSheet,
                                scope = coroutineScope
                            )
                        }
                    }
                }
            }
        }
        healthButton.setOnClickListener {
            Log.d("DEBUG", "CLICK DETECTED")
            binding.prefsCompose.apply {
                setContent {
                    val scope = rememberCoroutineScope()
                    LaunchedEffect(key1 = Unit) {
                        scope.launch {
                            prefViewModel.toggleBottomSheet()
                        }
                    }
                }
            }

        }

        for (mealType in resources.getStringArray(R.array.meal_types_array)) {
            val chip = Chip(this)
            chip.text = mealType
            chip.isCheckable = true
            chip.tag = mealType.lowercase()
            mealTypeChipGroup.addView(chip)
        }

        for (minute in resources.getIntArray(R.array.minutes_array)) {
            val chip = Chip(this)
            chip.text = "$minute min"
            chip.isCheckable = true
            chip.tag = minute
            minuteChipGroup.addView(chip)
        }

        cuisineDialog.setItems(resources.getStringArray(R.array.cuisines_array))


        searchBar.setOnQueryTextListener(object: android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val mealTypeChipIds = mealTypeChipGroup.checkedChipIds
                    val mealTypes = mealTypeChipIds.map { mealTypeChipGroup.findViewById<Chip>(it).tag as String }.joinToString(",")

                    val minuteChipId = minuteChipGroup.checkedChipId
                    val minutes = if (minuteChipId >= 0)  minuteChipGroup.findViewById<Chip>(minuteChipId).tag as? Int else null

                    val calories = caloriesSlider.values
                    val minCalories = calories[0].toInt()
                    val maxCalories = calories[1].toInt()

                    val cuisines = cuisineDialog.getItems().joinToString(",")
                    val diets  = ""
                    val intolerances = ""
                    for(pref in prefViewModel.getActivePrefs()){
                        if(pref.category == "diet") {
                            diets.plus(pref.name.plus(","))
                        } else if (pref.category == "intolerances") {
                            intolerances.plus(pref.name.substringBefore("-Free").plus(","))
                        }
                    }

                    binding.root.clearFocus()
                    searchFilters.visibility = android.view.View.GONE
                    searchResults.visibility = android.view.View.GONE
                    loading.visibility = android.view.View.VISIBLE
                    searchRecipes(query, mealTypes, minutes, minCalories, maxCalories, cuisines,
                        diets, intolerances)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        filterButton.setOnClickListener {
            searchResults.visibility = android.view.View.GONE
            searchFilters.visibility = android.view.View.VISIBLE
        }

        setContentView(binding.root)
    }

    private fun searchRecipes(query: String, mealTypes: String? = null,
            minutes: Int? = null, minCalories: Int? = null,
            maxCalories: Int? = null, cuisine: String? = null,
            diets: String? = null, intolerances: String? = null) {

        val call = RetrofitClient.recipeServiceInstance.searchRecipes(
            q = query,
            maxReadyTime = minutes,
            minCalories = minCalories,
            maxCalories = maxCalories,
            type = mealTypes,
            cuisine = cuisine,
            diet = diets,
            intolerances = intolerances)
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

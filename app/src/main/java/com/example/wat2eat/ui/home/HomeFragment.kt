package com.example.wat2eat.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import android.icu.util.Calendar
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wat2eat.api.RecipeResponse
import com.example.wat2eat.api.RetrofitClient
import com.example.wat2eat.databinding.FragmentHomeBinding
import com.google.android.play.core.integrity.l
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.wat2eat.ui.home.SearchActivity
import com.example.wat2eat.ui.recipe.RecipeActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var dummyRecipes : RecipeAdapter


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.greeting.text = getGreetingText()

        binding.searchView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                openSearchActivity()
            }
        })

        binding.searchView.setOnQueryTextFocusChangeListener(object: View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    openSearchActivity()
                }
            }
        })

        categoryAdapter = CategoryAdapter()
        binding.rvMainCategory.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        categoryAdapter.submitList(homeViewModel.getMainCategories())

        dummyRecipes = RecipeAdapter() {
            val intent = Intent(context, RecipeActivity::class.java)
            // TODO: replace with actual recipe data
            val extras = Bundle().apply {
                putString("EXTRA_USERNAME", "my_username")
                putString("EXTRA_PASSWORD", "my_password")
            }
            intent.putExtras(extras)
            startActivity(Intent(intent))
        }
        binding.rvSubCategory.apply {
            adapter = dummyRecipes
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        dummyRecipes.submitList(homeViewModel.getDummyRecipes())

        return root
    }

    private fun getGreetingText(): String {
        val baseGreeting = "Not sure what to cook "
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (currentHour) {
            in 5..10 -> baseGreeting + "this morning?"
            in 11..13 -> baseGreeting + "for lunch?"
            in 14..17 -> baseGreeting + "this afternoon?"
            in 18..23 -> baseGreeting + "tonight?"
            else -> baseGreeting + "for a late night snack?"
        }
    }

    private fun openSearchActivity() {
        binding.searchView.clearFocus()
        startActivity(Intent(context, SearchActivity::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
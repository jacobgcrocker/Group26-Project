package com.example.wat2eat.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import android.icu.util.Calendar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wat2eat.api.RecipeResponse
import com.example.wat2eat.api.RetrofitClient
import com.example.wat2eat.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchRecipes(query)
                } else {
                    Toast.makeText(context, "Please enter a search query", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                //TODO
                return true
            }
        })

        categoryAdapter = CategoryAdapter()
        binding.rvMainCategory.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        categoryAdapter.submitList(homeViewModel.getMainCategories())

        dummyRecipes = RecipeAdapter()
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

    private fun searchRecipes(query: String) {
        val call = RetrofitClient.instance.searchRecipes(q = query)
        call.enqueue(object: Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                val responses = response.body()?.hits
                Log.i("response:", responses.toString())
            }
            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                print(t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
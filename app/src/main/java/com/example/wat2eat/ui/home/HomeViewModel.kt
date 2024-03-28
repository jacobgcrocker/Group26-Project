package com.example.wat2eat.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wat2eat.models.Recipe

class HomeViewModel : ViewModel() {

    // Dummy data
    fun getMainCategories(): List<Category> {
        return listOf(
            Category("Breakfast", "https://www.themealdb.com/images/category/breakfast.png"),
            Category("Dessert", "https://www.themealdb.com/images/category/dessert.png"),
            Category("Vegetarian", "https://www.themealdb.com/images/category/vegetarian.png"),
            Category("Breakfast", "https://www.themealdb.com/images/category/breakfast.png"),
            Category("Dessert", "https://www.themealdb.com/images/category/dessert.png"),
            Category("Vegetarian", "https://www.themealdb.com/images/category/vegetarian.png")
        )
    }

    fun getDummyRecipes(): List<Recipe> {
        return listOf(
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
        )
    }

    data class Category(val name: String, val imageUrl: String)

}
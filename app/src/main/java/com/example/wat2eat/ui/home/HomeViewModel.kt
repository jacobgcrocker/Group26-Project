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
            Recipe("1","Pancakes", "https://www.themealdb.com/images/media/meals/rwuyqx1511383174.jpg"),
            Recipe("2","Pancakes", "https://www.themealdb.com/images/media/meals/rwuyqx1511383174.jpg"),
            Recipe("3","Pancakes", "https://www.themealdb.com/images/media/meals/rwuyqx1511383174.jpg"),
            Recipe("4","Pancakes", "https://www.themealdb.com/images/media/meals/rwuyqx1511383174.jpg"),
            Recipe("5","Pancakes", "https://www.themealdb.com/images/media/meals/rwuyqx1511383174.jpg"),
            Recipe("6","Pancakes", "https://www.themealdb.com/images/media/meals/rwuyqx1511383174.jpg"),
            Recipe("7","Pancakes", "https://www.themealdb.com/images/media/meals/rwuyqx1511383174.jpg"),
            Recipe("8","Pancakes", "https://www.themealdb.com/images/media/meals/rwuyqx1511383174.jpg"),
            Recipe("9","Pancakes", "https://www.themealdb.com/images/media/meals/rwuyqx1511383174.jpg"),

        )
    }

    data class Category(val name: String, val imageUrl: String)

}
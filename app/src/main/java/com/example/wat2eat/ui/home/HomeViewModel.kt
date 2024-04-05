package com.example.wat2eat.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wat2eat.models.Recipe

class HomeViewModel : ViewModel() {

    // Dummy data
    fun getMainCategories(): List<Category> {
        return listOf(
            Category("Breakfast", "https://i.postimg.cc/268Nt3RZ/breakfast.png"),
            Category("Dessert", "https://i.postimg.cc/br4XdTGx/dessert.png"),
            Category("Dinner", "https://i.postimg.cc/J4BVv9gP/dinner.png"),
            Category("Healthy", "https://i.postimg.cc/1X8h9TzR/healthy.png"),
            Category("Lunch", "https://i.postimg.cc/J7FCT1Ts/lunch.png"),
            Category("Quick & Easy", "https://i.postimg.cc/HxZ11HdP/quick.png")
        )
    }

    fun getDummyRecipes(): List<Recipe> {
        return listOf(
            Recipe(658546,"Roasted Chicken and Brown Rice Soup", "https://img.spoonacular.com/recipes/658546-312x231.jpg", "jpg"),
            Recipe(1095693,"Raspberry Arugula Side Salad", "https://img.spoonacular.com/recipes/1095693-312x231.jpg", "jpg"),
            Recipe(658269,"Rice Noodle Salad", "https://img.spoonacular.com/recipes/658269-312x231.jpg", "jpg"),
            Recipe(655145,"Peach Pie", "https://img.spoonacular.com/recipes/655145-312x231.jpg", "jpg"),
            Recipe(643514,"Fresh Herb Omlette", "https://img.spoonacular.com/recipes/643514-312x231.jpg", "jpg"),
            Recipe(642601,"Farro Salad", "https://img.spoonacular.com/recipes/642601-312x231.jpg", "jpg"),
            Recipe(1096025,"Steak Salad with Chimichurri Sauce", "https://img.spoonacular.com/recipes/1096025-312x231.jpg", "jpg"),
            Recipe(638316,"Chicken Shawarma Bowl", "https://img.spoonacular.com/recipes/638316-312x231.jpg", "jpg"),
            Recipe(637591,"Cheese Tortellini Alfredo", "https://img.spoonacular.com/recipes/637591-312x231.jpg", "jpg"),
            Recipe(716342,"Chicken Suya", "https://spoonacular.com/recipeImages/716342-312x231.jpg", "jpg"),
        )
    }

    data class Category(val name: String, val imageUrl: String)

}
package com.example.wat2eat.models

// TODO: add method to parse DetailedRecipe into SimplifiedRecipe
data class SimplifiedRecipe(
    val title: String,
    val image: String,
    val servings: Int,
    val readyInMinutes: Int,
    // the response doesn't have a calories field, but it's in the summary
    val calories: Int,
    val ingredients: List<Ingredient>,
    val instructions: List<String>,
)

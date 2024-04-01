package com.example.wat2eat.models

data class DetailedRecipe(
    val id: Int,
    val title: String,
    val image: String,
    val servings: Int,
    val readyInMinutes: Int,
    val extendedIngredients: List<Ingredient>,
    // the instructions field is a string with no formatting
    // use analyzedInstructions instead
    val analyzedInstructions: List<AnalyzedInstruction>,
)
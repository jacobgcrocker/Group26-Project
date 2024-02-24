package com.example.wat2eat.api

import com.example.wat2eat.models.Recipe

data class RecipeResponse(
    val hits: List<Hit>,
)

data class Hit(
    val recipe: Recipe,
)

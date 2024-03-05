package com.example.wat2eat.models

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    // TODO: ADD INGREDIENTS, DIET LABELS, HEALTH LABELS, CALORIES, ETC.
)
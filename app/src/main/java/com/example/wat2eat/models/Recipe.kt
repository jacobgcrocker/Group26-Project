package com.example.wat2eat.models

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String,
    // TODO: ADD INGREDIENTS, DIET LABELS, HEALTH LABELS, CALORIES, ETC.
    // val readyInMinutes: Int,
    // val servings: Int,
    // val nutrition: Nutrition
)

data class Nutrition(
    val nutrients: List<Nutrient>
)

data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String,
    val percentOfDailyNeeds: Double
)

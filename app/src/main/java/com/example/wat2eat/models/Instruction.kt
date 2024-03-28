package com.example.wat2eat.models

data class Step(
    val number: Int,
    val step: String,
)

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>,
)
package com.example.wat2eat.models

data class Equipment(
    val id: Int,
    val name: String,
)

data class Step(
    val number: Int,
    // step: instruction
    val step: String,
    val equipment: List<Equipment>,
)

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>,
)
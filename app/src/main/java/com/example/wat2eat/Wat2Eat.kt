package com.example.wat2eat

import android.app.Application
import com.example.wat2eat.ui.reviews.ReviewViewModelFactory

class Wat2Eat : Application() {
    val reviewViewModelFactory: ReviewViewModelFactory by lazy {
        ReviewViewModelFactory()
    }
}
package com.example.wat2eat.ui.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wat2eat.models.Review
import com.example.wat2eat.api.RetrofitClient
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ReviewViewModel : ViewModel() {
    private val _reviewList = MutableLiveData<List<Review>>()
    val reviewList: LiveData<List<Review>> = _reviewList

    fun addReview(newReview: Review) {
        val updatedList = _reviewList.value.orEmpty().toMutableList().apply {
            add(newReview)
        }
        _reviewList.value = updatedList
    }

    fun updateReviewList(updatedReviews: List<Review>) {
        _reviewList.value = updatedReviews
    }

    fun fetchReviewsByRecipeId(recipeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val reviews = RetrofitClient.reviewServiceInstance.getReviewsByRecipeId(recipeId)
                withContext(Dispatchers.Main) {
                    _reviewList.value = reviews
                }
            } catch (e: Exception) {
                println("Error fetching reviews: $e")
            }
        }
    }
}
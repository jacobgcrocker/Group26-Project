package com.example.wat2eat.ui.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wat2eat.models.Review
import com.example.wat2eat.api.RetrofitClient
import androidx.lifecycle.viewModelScope
import com.example.wat2eat.models.ReviewWithDescription
import com.example.wat2eat.models.StoreReview
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
        postReview(newReview)
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

    fun postReview(newReview: Review) {
        val storeReview = convertReview(newReview)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.reviewServiceInstance.postReview(storeReview)
                if (response.isSuccessful) {
                    fetchReviewsByRecipeId(newReview.recipeId.toString())
                } else {
                    withContext(Dispatchers.Main) {
                        println("Failed to post review: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Exception posting review: $e")
                }
            }
        }
    }

    fun convertReview(review: Review): StoreReview {
        return StoreReview(
            reviewId = review.reviewId,
            userId = review.userId,
            rating = review.rating,
            content = if (review is ReviewWithDescription) review.getDetails() else null
        )
    }
}
package com.example.wat2eat.ui.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wat2eat.models.Review
import com.example.wat2eat.api.RetrofitClient
import androidx.lifecycle.viewModelScope
import com.example.wat2eat.models.BasicReview
import com.example.wat2eat.models.ReviewWithDescription
import com.example.wat2eat.models.StoreReview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                val convertedReviews = reviews.map { storeReview ->
                    reconstructReview(storeReview)
                }
                withContext(Dispatchers.Main) {
                    _reviewList.value = convertedReviews
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Exception or failure fetching reviews: $e")
                }
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
            recipeId = review.recipeId,
            username = review.username,
            userId = review.userId,
            rating = review.rating,
            content = if (review is ReviewWithDescription) review.getDetails() else null
        )
    }

    fun reconstructReview(storeReview: StoreReview): Review {
        var review: Review = BasicReview(
            reviewId = storeReview.reviewId,
            recipeId = storeReview.recipeId,
            userId = storeReview.userId,
            username = storeReview.username,
            image = null,
            rating = storeReview.rating
        )
        storeReview.content?.let { description ->
            review = ReviewWithDescription(review, description)
        }
        return review
    }

    fun deleteReview(review: Review) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.reviewServiceInstance.deleteReview(review.reviewId)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        fetchReviewsByRecipeId(review.recipeId.toString())
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        println("Failed to delete review: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Exception deleting review: $e")
                }
            }
        }
    }
}
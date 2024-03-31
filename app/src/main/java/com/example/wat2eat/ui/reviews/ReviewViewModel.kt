package com.example.wat2eat.ui.reviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wat2eat.models.Review

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
}
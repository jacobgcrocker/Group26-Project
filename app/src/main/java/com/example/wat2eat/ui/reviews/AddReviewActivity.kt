package com.example.wat2eat.ui.reviews
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.wat2eat.models.Review
import com.example.wat2eat.databinding.ActivityAddReviewBinding

class AddReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReviewBinding
    //db
    //storage
    //
    private val image: String? = null
    private var userId: String? = null
    private var username: String? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(PARAM_USER_ID) && intent.hasExtra(PARAM_USER_NAME)) {
            userId = intent.getStringExtra(PARAM_USER_ID)
            username = intent.getStringExtra(PARAM_USER_NAME)
        } else {
            Toast.makeText(this, "Error cannot create review", Toast.LENGTH_LONG).show()
            finish()
        }
        binding.addReviewProgressLayout.setOnTouchListener { v, event -> true }
    }

    fun addImage(v: View) {

    }

    fun goBack(v: View) {
        finish()
    }

    private val reviewList = mutableListOf<Review>()
    fun submitReview(v : View) {
        binding.addReviewProgressLayout.visibility = View.VISIBLE
        val description = binding.reviewDescription.text.toString()
        val rating = binding.ratingBar.rating

        val reviewId = (reviewList.size + 1).toString()
        val review = Review(reviewId, userId ?: "", username ?: "", description, image,
            System.currentTimeMillis(), rating)
        reviewList.add(review)

        Toast.makeText(this, "Review submitted successfully!", Toast.LENGTH_SHORT).show()
        finish()
    }
    companion object {
        val PARAM_USER_ID = "UserId"
        val PARAM_USER_NAME = "UserName"

        fun newIntent(context: Context, userId: String?, userName: String?): Intent {
            val intent = Intent(context, AddReviewActivity::class.java)
            intent.putExtra(PARAM_USER_ID, userId)
            intent.putExtra(PARAM_USER_NAME, userName)
            return intent
        }
    }
}
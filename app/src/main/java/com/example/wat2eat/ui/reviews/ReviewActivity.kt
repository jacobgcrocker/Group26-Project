package com.example.wat2eat.ui.reviews

import ReviewAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wat2eat.R
import com.example.wat2eat.Wat2Eat
import com.example.wat2eat.data.auth.UserRepository
import com.example.wat2eat.databinding.ActivityReviewBinding
import com.google.firebase.auth.FirebaseAuth


class ReviewActivity : AppCompatActivity() {
    val userRepository = UserRepository.getInstance()
    val user = userRepository.getLoggedInUser()
    private var userId = FirebaseAuth.getInstance().currentUser?.uid
    private var username = user?.username ?: "Anonymous"
    private lateinit var binding: ActivityReviewBinding
    private lateinit var viewModel: ReviewViewModel
    private lateinit var adapter: ReviewAdapter
    private var recipeId: Int = -1


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = (application as Wat2Eat).reviewViewModelFactory
        viewModel = ViewModelProvider(this, viewModelFactory).get(ReviewViewModel::class.java)

        adapter = ReviewAdapter()

        with(binding.reviewList) {
            layoutManager = LinearLayoutManager(this@ReviewActivity)
            adapter = this@ReviewActivity.adapter
        }

        viewModel.reviewList.observe(this) { reviews ->
            adapter.updateReviewList(reviews)
        }

        binding.addReviewButton.setOnClickListener {
            showAddReviewFragment()
        }

        binding.goBack.setOnClickListener {
            finish()
        }

        recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, -1)
        if (recipeId != -1) {
            viewModel.fetchReviewsByRecipeId(recipeId.toString())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.addReviewButton.visibility = View.VISIBLE
        binding.goBack.visibility = View.VISIBLE
    }

    private fun showAddReviewFragment() {
        hideButtons()
        val fragment = AddReviewFragment.newInstance(userId ?: "",
            username ?: "Anonymous", recipeId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun showButtons() {
        binding.addReviewButton.visibility = View.VISIBLE
        binding.goBack.visibility = View.VISIBLE
    }

    fun hideButtons() {
        binding.addReviewButton.visibility = View.GONE
        binding.goBack.visibility = View.GONE
    }
    companion object {
        const val EXTRA_RECIPE_ID = "com.example.wat2eat.extra.RECIPE_ID"

        fun newIntent(context: Context, recipeId: Int): Intent {
            return Intent(context, ReviewActivity::class.java).apply {
                putExtra(EXTRA_RECIPE_ID, recipeId)
            }
        }
    }
}
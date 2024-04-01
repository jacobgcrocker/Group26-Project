package com.example.wat2eat.ui.reviews

import ReviewAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wat2eat.R
import com.example.wat2eat.Wat2Eat
import com.example.wat2eat.data.auth.UserRepository
import com.example.wat2eat.databinding.ActivityReviewBinding
import com.example.wat2eat.databinding.ActivityReviewBinding.inflate
import com.example.wat2eat.models.Review
import com.example.wat2eat.models.User
import com.google.firebase.auth.FirebaseAuth


class ReviewActivity : AppCompatActivity() {
    val userRepository = UserRepository.getInstance()
    val user = userRepository.getLoggedInUser()
    private var userId = FirebaseAuth.getInstance().currentUser?.uid
    private var username = user?.username ?: "Anonymous"
    private lateinit var binding: ActivityReviewBinding
    private lateinit var viewModel: ReviewViewModel
    private lateinit var adapter: ReviewAdapter

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
            adapter = this@ReviewActivity.adapter // Set the already initialized adapter
        }

        viewModel.reviewList.observe(this) { reviews ->
            adapter.updateReviewList(reviews)
        }

        binding.addReviewButton.setOnClickListener {
            showAddReviewFragment()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.addReviewButton.visibility = View.VISIBLE
        binding.goBack.visibility = View.VISIBLE
    }

    private fun showAddReviewFragment() {
        hideButtons()
        val fragment = AddReviewFragment.newInstance(userId ?: "", username ?: "Anonymous")
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null) // Optional: Add transaction to back stack
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

    fun exit() {
        supportFragmentManager.popBackStack()
    }
    companion object {
        fun newIntent(context: Context) = Intent(context, ReviewActivity::class.java)
    }
}
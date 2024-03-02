package com.example.wat2eat.ui.reviews

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wat2eat.databinding.ActivityReviewBinding
import com.example.wat2eat.databinding.ActivityReviewBinding.inflate
import com.google.firebase.auth.FirebaseAuth


class ReviewActivity : AppCompatActivity() {

    private var userId = FirebaseAuth.getInstance().currentUser?.uid

    //private var user: User? = null
    private var username = "jim"
    private lateinit var binding: ActivityReviewBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflate(layoutInflater)
        setContentView(binding.root)

        binding.addReviewButton.setOnClickListener {
            startActivity(AddReviewActivity.newIntent(this, userId, username))
        }

        binding.reviewProgressLayout.setOnTouchListener { v, event -> true }

    }
    companion object {
        fun newIntent(context: Context) = Intent(context, ReviewActivity::class.java)
    }
}
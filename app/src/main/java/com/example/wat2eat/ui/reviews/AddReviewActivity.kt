package com.example.wat2eat.ui.reviews
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.wat2eat.models.Review
import com.example.wat2eat.databinding.ActivityAddReviewBinding
import java.io.File
import java.io.FileOutputStream

class AddReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReviewBinding
    //db
    //storage
    //
    private var image: String? = null
    private var userId: String? = null
    private var username: String? = null
    private val reviewList = mutableListOf<Review>()

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                // Handle the selected image URI
                image = uri.toString()
                saveImageLocally(uri)
            }
        }
    }
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
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooserIntent = Intent.createChooser(intent, "Select Image")
        pickImageLauncher.launch(chooserIntent)
    }

    fun goBack(v: View) {
        finish()
    }

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

    private fun saveImageLocally(imageUri: Uri?): String? {
        if (imageUri == null) return null

        val inputStream = contentResolver.openInputStream(imageUri)
        val imageName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(cacheDir, imageName)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
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
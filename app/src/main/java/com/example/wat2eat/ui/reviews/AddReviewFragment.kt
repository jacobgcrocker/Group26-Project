package com.example.wat2eat.ui.reviews

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.example.wat2eat.databinding.FragmentAddReviewBinding
import com.example.wat2eat.models.BasicReview
import com.example.wat2eat.models.Review
import com.example.wat2eat.models.ReviewWithDescription
import java.io.File
import java.io.FileOutputStream

class AddReviewFragment : Fragment() {

    private var _binding: FragmentAddReviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReviewViewModel by activityViewModels()
    private var image: String? = null
    private var userId: String? = null
    private var username: String? = null
    private var recipeId: Int? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                image = uri.toString()
                saveImageLocally(uri)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getString(PARAM_USER_ID)
        username = arguments?.getString(PARAM_USER_NAME)
        recipeId = arguments?.getInt(PARAM_RECIPE_ID)

        if (userId == null || username == null) {
            Toast.makeText(context, "Error cannot create review", Toast.LENGTH_LONG).show()
            return
        }

        binding.cancelButton.setOnClickListener{
            parentFragmentManager.popBackStack()
            makeButtonsVisible()
        }
        binding.reviewImage.setOnClickListener {
            addImage()
        }

        binding.submit.setOnClickListener {
            submitReview()
        }

        binding.username.text = username

        binding.addReviewProgressLayout.setOnTouchListener { v, event -> true }
    }

    private fun addImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        pickImageLauncher.launch(Intent.createChooser(intent, "Select Image"))
    }

    private fun submitReview() {
        binding.addReviewProgressLayout.visibility = View.VISIBLE
        val description = binding.reviewDescription.text.toString()
        val rating = binding.ratingBar.rating

        val reviewId = ((viewModel.reviewList.value?.size ?: (0 + 1))).toString()
        val review = BasicReview(reviewId, recipeId, userId ?: "", username ?: "", image, rating)
        val finalReview = if (description.isNotEmpty()) {
            ReviewWithDescription(review, description)
        } else {
            review
        }

        viewModel.addReview(finalReview)
        binding.addReviewProgressLayout.visibility = View.GONE

        Toast.makeText(requireContext(), "Review submitted successfully!", Toast.LENGTH_SHORT).show()

        parentFragmentManager.popBackStack()
        makeButtonsVisible()
    }

    private fun saveImageLocally(imageUri: Uri?): String? {
        val context = context ?: return null
        val inputStream = context.contentResolver.openInputStream(imageUri ?: return null)
        val imageName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(context.cacheDir, imageName)
        val outputStream = FileOutputStream(file)

        inputStream.use { input ->
            outputStream.use { output ->
                input?.copyTo(output)
            }
        }

        return file.absolutePath
    }

    private fun makeButtonsVisible() {
        (activity as? ReviewActivity)?.showButtons()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PARAM_USER_ID = "UserId"
        const val PARAM_USER_NAME = "UserName"
        const val PARAM_RECIPE_ID = "RecipeId"

        fun newInstance(userId: String, userName: String, recipeId: Int): AddReviewFragment {
            val fragment = AddReviewFragment()
            val args = Bundle().apply {
                putString(PARAM_USER_ID, userId)
                putString(PARAM_USER_NAME, userName)
                putInt(PARAM_RECIPE_ID, recipeId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
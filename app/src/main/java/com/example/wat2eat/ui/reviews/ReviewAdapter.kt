import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wat2eat.databinding.ItemRvReviewBinding
import com.example.wat2eat.models.Review

class ReviewAdapter(private val userId: String?,
                    private val onDeleteClick: (Review) -> Unit)
    : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var reviewList: List<Review> = emptyList()

    inner class ReviewViewHolder(private val binding: ItemRvReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.username.text = review.username
            binding.reviewComment.text = review.getDetails()
            binding.ratingBar.rating = review.rating!!

            if (review.userId == userId) {
                binding.deleteReviewButton.visibility = View.VISIBLE
                binding.deleteReviewButton.setOnClickListener {
                    onDeleteClick(review)
                }
            } else {
                binding.deleteReviewButton.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemRvReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateReviewList(newList: List<Review>) {
        reviewList = newList
        notifyDataSetChanged()
    }
}
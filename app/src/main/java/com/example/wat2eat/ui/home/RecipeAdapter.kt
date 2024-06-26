package com.example.wat2eat.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wat2eat.databinding.ItemRvSubCategoryBinding
import com.example.wat2eat.models.Recipe
import com.squareup.picasso.Picasso

class RecipeAdapter(
    private val listener: (Recipe) -> Unit
) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRvSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
        holder.itemView.setOnClickListener { listener(recipe) }
    }

    class ViewHolder(private val binding: ItemRvSubCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.tvDishName.text = recipe.title
            // binding.tvTime.text = "${recipe.readyInMinutes} min"
            // val calories = recipe.nutrition.nutrients[0]
            // binding.tvCalories.text = "${calories.amount} ${calories.unit}"
            Picasso.get()
                .load(recipe.image)
                .fit()
                .centerCrop()
                .into(binding.imgDish)
        }
    }

    private class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }
}

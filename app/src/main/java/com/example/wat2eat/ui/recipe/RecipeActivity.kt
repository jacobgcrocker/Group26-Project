package com.example.wat2eat.ui.recipe

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wat2eat.R
import com.example.wat2eat.api.RetrofitClient
import com.example.wat2eat.models.DetailedRecipe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        val extras = intent.extras

        val recipeId = extras?.getString("recipe_id")
        Log.i("recipe_id", recipeId.toString())
        if (recipeId == null) {
            Log.e("error", "no recipe_id")
        } else {
            getRecipe(recipeId.toInt())
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RecipeFragment())
                .commit()
        }
    }

    private fun getRecipe(recipeId: Int) {
        val call = RetrofitClient.instance.getRecipeInformation(recipeId)
        call.enqueue(object : Callback<DetailedRecipe> {
            override fun onResponse(
                call: Call<DetailedRecipe>,
                response: Response<DetailedRecipe>
            ) {
                val recipe = response.body()
                Log.i("Detailed recipe:", recipe.toString())
                if (recipe == null) {
                    Toast.makeText(
                        this@RecipeActivity,
                        "Error fetching recipe!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val fragment =
                        supportFragmentManager.findFragmentById(R.id.container) as? RecipeFragment
                    fragment?.updateRecipeDetails(recipe)
                }
            }

            override fun onFailure(call: Call<DetailedRecipe>, t: Throwable) {
                print(t)
            }
        })
    }
}

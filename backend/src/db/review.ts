import mongoose from "mongoose";

const ReviewSchema = new mongoose.Schema({
  userId: { type: String, required: true },
  recipeId: { type: String, required: true },
  content: { type: String },
  rating: { type: Number },
});

export const Review = mongoose.model("Review", ReviewSchema);

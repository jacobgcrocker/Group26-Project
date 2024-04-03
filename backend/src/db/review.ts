import mongoose from "mongoose";

const ReviewSchema = new mongoose.Schema({
	userId: { type: String },
	username: {type: String},
	recipeId: { type: Number },
	reviewId: { type: Number },
	content: { type: String },
	rating: { type: Number },
});

export const Review = mongoose.model("Review", ReviewSchema);

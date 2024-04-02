import mongoose from 'mongoose';
import { UserDataSchema } from './userData';

const ReviewSchema = new mongoose.Schema({
	userId: { type: String },
	recipeId: { type: String },
	content: { type: String },
	rating: { type: Number },
});

export const Review = mongoose.model('Review', ReviewSchema);

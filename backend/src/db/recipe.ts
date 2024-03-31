import mongoose from 'mongoose';

export const RecipeSchema = new mongoose.Schema({
	id: { type: String },
	title: { type: String },
	image: { type: String },
	servings: { type: Number },
	readyInMinutes: { type: Number },
	extendedIngredients: { type: Array },
	analyzedInstructions: { type: Array },
});

export const Recipe = mongoose.model('Recipe', RecipeSchema);

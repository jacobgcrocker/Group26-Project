import express from 'express';
import {
	createRecipe,
	getRecipeById,
	getRecipesByIds,
} from '../controllers/recipe';

export default (router: express.Router) => {
	router.post('/recipe', createRecipe);
	router.get('/recipe', getRecipeById);
	router.get('/recipes', getRecipesByIds);
};

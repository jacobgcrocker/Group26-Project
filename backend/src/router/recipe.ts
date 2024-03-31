import express from 'express';
import { createRecipe } from '../controllers/recipe';

export default (router: express.Router) => {
	router.post('/recipe', createRecipe);
};

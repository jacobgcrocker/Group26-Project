import express from 'express';
import { Recipe } from '../db/recipe';

export const createRecipe = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const recipe = await Recipe.create(req.body);
		res.status(200).json(recipe);
	} catch (error) {
		console.error(error);
		res.status(500).json({ error });
	}
};

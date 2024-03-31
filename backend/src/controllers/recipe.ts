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

export const getRecipeById = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const recipe = await Recipe.findOne({ id: req.query.recipeId });
		res.status(200).json(recipe);
	} catch (error) {
		console.error(error);
		res.status(500).json({ error });
	}
};

export const getRecipesByIds = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const recipes = await Recipe.find({ id: { $in: req.body.recipeIds } });
		res.status(200).json(recipes);
	} catch (error) {
		console.error(error);
		res.status(500).json({ error });
	}
};

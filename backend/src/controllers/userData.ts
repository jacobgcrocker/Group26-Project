import express from 'express';
import { UserData } from '../db/userData';
import { Recipe } from 'db/recipe';

export const createUserData = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const { email } = req.body;
		const userData = await UserData.create({ email });
		res.status(200).json(userData);
	} catch (error) {
		res.status(400).json({ error });
	}
};

export const getUserData = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const { email } = req.params;
		const userData = await UserData.findOne({ email });
		res.status(200).json(userData);
	} catch (error) {
		res.status(400).json({ error });
	}
};

export const updateUserData = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const { email, ...rest } = req.body;
		const userData = await UserData.findOneAndUpdate(
			{ email },
			{ rest },
			{ new: true } // Return the updated document
		);
		res.status(200).json(userData);
	} catch (error) {
		res.status(400).json({ error });
	}
};

export const deleteUserData = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const { email } = req.params;
		const deletedUserData = await UserData.deleteOne({ email });
		res.status(200).json(deletedUserData);
	} catch (error) {
		res.status(400).json({ error });
	}
};

export const updateUserFavouriteRecipes = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const { userId, recipeId, favourite } = req.body;
		// favourite = true to add to favourites, false to remove from favourites

		const user = await UserData.findOne({ id: userId });
		if (!user) {
			res.status(404).json({ error: 'User not found' });
			return;
		}

		if (favourite) {
			user.favourites.push(recipeId);
		} else {
			user.favourites = user.favourites.filter((id) => id !== recipeId);
		}

		await user.save();
		res.status(200).json(user);
	} catch (error) {
		console.error(error);
		res.status(500).json({ error });
	}
};

export const getUserFavouriteRecipes = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const { userId } = req.params;
		const user = await UserData.findOne({ id: userId });
		if (!user) {
			res.status(404).json({ error: 'User not found' });
			return;
		}

		const recipes = await Recipe.find({ id: { $in: user.favourites } });
		res.status(200).json(recipes);
	} catch (error) {
		console.error(error);
		res.status(500).json({ error });
	}
};

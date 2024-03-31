import express from 'express';
import { Review } from '../db/review';

export const createReview = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const review = await Review.create(req.body);
		// 200 by default
		res.json(review);
	} catch (error) {
		console.error(error);
		res.status(500).json({ error });
	}
};

export const getReviewsByRecipeID = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		const { reviewId } = req.params;
		const reviews = await Review.find({ recipeId: reviewId });
		res.json(reviews);
	} catch (error) {
		console.error(error);
		res.status(500).json({ error });
	}
};

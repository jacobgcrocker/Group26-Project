import express from 'express';
import { Review } from '../db/review';

export const createReview = async (
	req: express.Request,
	res: express.Response
) => {
	try {
		console.log(req.body)
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
		const { recipeId } = req.query;
		const reviews = await Review.find({ recipeId });
		res.json(reviews);
	} catch (error) {
		console.error(error);
		res.status(500).json({ error });
	}
};

export const deleteReview = async (
	req: express.Request, 
	res: express.Response
) => {
    try {
        const { reviewId } = req.params;
        await Review.findByIdAndDelete(reviewId);
        res.status(204).send();
    } catch (error) {
        console.error(error);
        res.status(500).json({ error });
    }
};

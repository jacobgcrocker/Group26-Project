import express from 'express';
import { createReview, getReviewsByRecipeID } from '../controllers/review';
import { deleteReview } from '../controllers/review';

export default (router: express.Router) => {
	router.post('/review', createReview);
	router.get('/review', getReviewsByRecipeID);
	router.delete('/review/:reviewId', deleteReview);
};

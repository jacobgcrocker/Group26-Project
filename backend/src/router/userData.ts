import express from 'express';
import {
	createUserData,
	getUserData,
	updateUserData,
	deleteUserData,
	updateUserFavouriteRecipes,
	getUserFavouriteRecipes,
} from '../controllers/userData';

export default (router: express.Router) => {
	router.post('/userData', createUserData);
	router.get('/userData/:email', getUserData);
	router.patch('/userData', updateUserData);
	router.delete('/userData/:email', deleteUserData);
	router.patch('/userData/favourites', updateUserFavouriteRecipes);
	router.get('/userData/favourites/:userId', getUserFavouriteRecipes);
};

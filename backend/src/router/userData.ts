import express from 'express';
import {
	createUserData,
	getUserData,
	updateUserData,
	deleteUserData,
	toggleUserFavouriteRecipe,
	isRecipeFavourite,
	getUserFavouriteRecipes,
} from '../controllers/userData';

export default (router: express.Router) => {
	router.post('/userData', createUserData);
	router.get('/userData', getUserData);
	router.patch('/userData', updateUserData);
	router.delete('/userData/:email', deleteUserData);
	router.patch('/userData/favourites', toggleUserFavouriteRecipe);
	router.get('/userData/favourite', isRecipeFavourite);
	router.get('/userData/favourites', getUserFavouriteRecipes);
};

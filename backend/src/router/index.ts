import express from 'express';
import health from './health';
import userData from './userData';
import recipe from './recipe';
import review from './review';

const router = express.Router();

export default (): express.Router => {
	health(router);
	userData(router);
	recipe(router);
	review(router);
	return router;
};

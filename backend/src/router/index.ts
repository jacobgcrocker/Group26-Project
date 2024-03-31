import express from "express";
import health from "./health";
import userData from "./userData";

const router = express.Router();

export default (): express.Router => {
  health(router);
  userData(router);
  return router;
};

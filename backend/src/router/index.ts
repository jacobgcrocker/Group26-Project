import express from "express";
import health from "./health";

const router = express.Router();

export default (): express.Router => {
  health(router);
  return router;
};

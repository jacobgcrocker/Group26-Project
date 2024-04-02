import express from "express";
import { health } from "../controllers/health";

export default (router: express.Router) => {
  router.get("/health", health);
};

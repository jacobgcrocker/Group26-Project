import express from "express";
import {
  createUserData,
  getUserData,
  updateUserData,
  deleteUserData,
} from "../controllers/userData";

export default (router: express.Router) => {
  router.post("/userData", createUserData);
  router.get("/userData/:email", getUserData);
  router.patch("/userData", updateUserData);
  router.delete("/userData/:email", deleteUserData);
};

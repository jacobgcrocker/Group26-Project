import express from "express";
import { UserData } from "../db/userData";

export const createUserData = async (
    req: express.Request,
    res: express.Response
) => {
    try {
        const { email, userId, displayName } = req.body;
        const userData = await UserData.create({ userId, email, displayName });
        res.status(200).json(userData);
    } catch (error) {
        console.log("error", error);
        res.status(400).json({ error });
    }
};

export const getUserData = async (
    req: express.Request,
    res: express.Response
) => {
    try {
        const { uid } = req.query;
        const userData = await UserData.findOne({ userId: uid });
        res.status(200).json(userData);
    } catch (error) {
        res.status(400).json({ error });
    }
};

export const updateUserData = async (
    req: express.Request,
    res: express.Response
) => {
    try {
        const { email, ...rest } = req.body;
        const userData = await UserData.findOneAndUpdate(
            { email },
            { rest },
            { new: true } // Return the updated document
        );
        res.status(200).json(userData);
    } catch (error) {
        res.status(400).json({ error });
    }
};

export const deleteUserData = async (
    req: express.Request,
    res: express.Response
) => {
    try {
        const { email } = req.params;
        const deletedUserData = await UserData.deleteOne({ email });
        res.status(200).json(deletedUserData);
    } catch (error) {
        res.status(400).json({ error });
    }
};

import express from "express";

export const health = async (req: express.Request, res: express.Response) => {
  return res.sendStatus(200);
};

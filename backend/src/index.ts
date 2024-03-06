import dotenv from "dotenv";
import express from "express";
import bodyParser from "body-parser";
import cors from "cors";
import http from "http";
import router from "./router";
import mongoose from "mongoose";

dotenv.config();

const app = express();
app.use(cors());
app.use(bodyParser.json());

const httpServer = http.createServer(app);
const PORT = process.env.PORT || 3000;

httpServer.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});

mongoose.set("strictQuery", true);
mongoose.Promise = Promise;
mongoose.connect(process.env.MONGODB_URL as string);
mongoose.connection.on("error", (err: Error) => console.log(err));

app.use("/", router());

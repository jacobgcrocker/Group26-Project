import dotenv from "dotenv";
import express from "express";
import bodyParser from "body-parser";
import cors from "cors";
import http from "http";

import router from "./router";

dotenv.config();

const app = express();
app.use(cors());
app.use(bodyParser.json());

const httpServer = http.createServer(app);
const PORT = process.env.PORT || 3000;

httpServer.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});

app.use("/", router());

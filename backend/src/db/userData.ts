import mongoose from "mongoose";

const SettingsSchema = new mongoose.Schema({
  theme: { type: String },
  textSize: { type: String },
  // TODO: Add more settings
});

const userDataSchema = new mongoose.Schema({
  // TODO: Or ID?
  email: {
    type: String,
    required: true,
    unique: true,
  },
  settings: {
    type: SettingsSchema,
  },
  favourites: {
    type: [String],
  },
});

export const UserData = mongoose.model("UserData", userDataSchema);

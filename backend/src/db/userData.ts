import mongoose from 'mongoose';

const SettingsSchema = new mongoose.Schema({
	theme: { type: String },
	textSize: { type: String },
	// TODO: Add more settings
});

export const UserDataSchema = new mongoose.Schema({
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
		// an array of recipe ids
		type: [String],
	},
});

export const UserData = mongoose.model('UserData', UserDataSchema);

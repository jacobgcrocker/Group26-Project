import mongoose from 'mongoose';

const SettingsSchema = new mongoose.Schema({
	theme: { type: String },
	textSize: { type: String },
	// TODO: Add more settings
});

const userDataSchema = new mongoose.Schema({
	userId: {
		type: String,
		required: true,
		unique: true,
	},
	email: {
		type: String,
		required: true,
	},
	displayName: {
		type: String,
		// required: true,
	},
	settings: {
		type: SettingsSchema,
	},
	favourites: {
		type: [String],
	},
});
export const UserData = mongoose.model('UserData', userDataSchema);

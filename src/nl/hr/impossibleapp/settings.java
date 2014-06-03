package nl.hr.impossibleapp;

public class settings {
	private static boolean soundEnabled = true;
	private static int lives = 3;
	private static int difficulty = 2;
	
	public static int getDifficulty() {
		return difficulty;
	}

	public static void setDifficulty(int difficulty) {
		settings.difficulty = difficulty;
	}
	
	public static boolean isSoundEnabled() {
		return soundEnabled;
	}

	public static void setSoundEnabled(boolean soundEnabled) {
		settings.soundEnabled = soundEnabled;
	}

	public static int getLives() {
		return lives;
	}

	public static void setLives(int lives) {
		settings.lives = lives;
	}
}

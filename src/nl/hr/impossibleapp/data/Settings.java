package nl.hr.impossibleapp.data;

import java.util.ArrayList;

import android.util.Log;

public class Settings {
	private static final String TAG = Settings.class.toString();
	private static boolean soundEnabled = true;
	private static int lives = 3;
	private static int difficulty = 1;
	private static int ScoreTotal = 0;
	private static ArrayList<Class<?>> gamesDone = new ArrayList<Class<?>>();
	private static String language = "NL";
	private static String name = null;
	
	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Settings.name = name;
	}

	public static void clearGamesDone(){
		gamesDone.clear();
	}

	public static ArrayList<Class<?>> getGamesDone() {
		return gamesDone;
	}

	public static void addGameGamesDone(Class<?> game){
		gamesDone.add(game);
	}

	public static int getDifficulty() {
		return difficulty;
	}

	public static void setDifficulty(int difficulty) {
		Settings.difficulty = difficulty;
	}
	
	public static boolean isSoundEnabled() {
		return soundEnabled;
	}

	public static void setSoundEnabled(boolean soundEnabled) {
		Settings.soundEnabled = soundEnabled;
	}

	public static int getLives() {
		return lives;
	}

	public static void setLives(int lives) {
		Settings.lives = lives;
	}
	
	public static int getScore() {
		return ScoreTotal;
	}

	public static void setScore(int score) {
		Settings.ScoreTotal = score;
	}
	
	public static void addScore(int score){
		Log.i("Settings", ""+score);
		if(score > 0){
			if(difficulty == 1){
				Settings.ScoreTotal = ScoreTotal + score;
			}else if(difficulty == 2){
				Settings.ScoreTotal = ScoreTotal + (int)(score * 1.5);
			}else if(difficulty == 3){
				Settings.ScoreTotal = ScoreTotal + (score * 2);
			}
		}
	}
	
	public static void resetAll(){
		lives = 3;
		difficulty = 1;
		ScoreTotal = 0;
		
		if(!gamesDone.isEmpty()){
			gamesDone.clear();
		}
		Log.i(TAG, "Reset");
	}

	public static String getLanguage() {
		return language;
	}

	public static void setLanguage(String language) {
		Settings.language = language;
	}
}

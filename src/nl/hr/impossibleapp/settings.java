package nl.hr.impossibleapp;

import java.util.ArrayList;

public class settings {
	private static boolean soundEnabled = true;
	private static int lives = 3;
	private static int difficulty = 1;
	private static int ScoreTotal = 0;
	private static boolean newGame = true;
	private static ArrayList<Class<?>> gamesList = new ArrayList<Class<?>>();
	private static ArrayList<Integer> gamesInstructions = new ArrayList<Integer>();
	private static ArrayList<Class<?>> gamesDone = new ArrayList<Class<?>>();
	private static ArrayList<Integer> instructionsDone = new ArrayList<Integer>();

	// reset done lists
	public static void resetDoneLists(){
		gamesDone.clear();
		instructionsDone.clear();
	}
	
	public static void setGamesList(ArrayList<Class<?>> gamesList) {
		settings.gamesList.addAll(gamesList);
	}

	public static void setGamesInstructions(ArrayList<Integer> gamesInstructions) {
		settings.gamesInstructions.addAll(gamesInstructions);
	}

	public static ArrayList<Class<?>> getGamesList() {
		return gamesList;
	}

	public static void addGameGamesList(Class<?> game){
		settings.gamesList.add(game);
	}
	
	public static void removeGamesList(Class<?> game){
		if(!gamesList.isEmpty()){
			settings.gamesList.remove(settings.gamesList.indexOf(game));
		}
	}
	
	public static ArrayList<Integer> getGamesInstructions() {
		return gamesInstructions;
	}

	public static void addGameInstructions(int instruction){
		settings.gamesInstructions.add(instruction);
	}
	
	public static void removeGameInstructions(int instruction){
		if(!gamesList.isEmpty()){
			settings.gamesInstructions.remove(settings.gamesInstructions.indexOf(instruction));
		}
	}
	
	public static void addInstructionsDone(int instruction){
		settings.instructionsDone.add(instruction);
	}
	
	public static ArrayList<Class<?>> getGamesDone() {
		return gamesDone;
	}

	public static void addGameGamesDone(Class<?> game){
		settings.gamesDone.add(game);
	}
	
	public static ArrayList<Integer> getGamesInstructionsDone() {
		return instructionsDone;
	}
	
	public static boolean isNewGame() {
		return newGame;
	}

	public static void setNewGame(boolean newGame) {
		settings.newGame = newGame;
	}

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
	
	public static int getScore() {
		return ScoreTotal;
	}

	public static void setScore(int score) {
		settings.ScoreTotal = score;
	}
	
	public static void resetAll(){
		lives = 3;
		difficulty = 1;
		ScoreTotal = 0;
		newGame = true;
		if(!gamesList.isEmpty()){
			gamesList.clear();
		}
		if(!gamesInstructions.isEmpty()){
			gamesInstructions.clear();
		}
		if(!gamesDone.isEmpty()){
			gamesDone.clear();
		}
		if(!instructionsDone.isEmpty()){
			instructionsDone.clear();
		}
		System.out.println("Reset");
	}
}

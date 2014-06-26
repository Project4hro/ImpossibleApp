package nl.hr.impossibleapp.activities;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import nl.hr.impossibleapp.Accelerometer;
import nl.hr.impossibleapp.Accelerometer2;
import nl.hr.impossibleapp.Hangman;
import nl.hr.impossibleapp.MemoryGame;
import nl.hr.impossibleapp.QuestionGamePicture;
import nl.hr.impossibleapp.Quiz;
import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.Shootgame;
import nl.hr.impossibleapp.SwipeGame1;
import nl.hr.impossibleapp.TargetGame;
import nl.hr.impossibleapp.TurnGame;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.gyrogame.GyroscopeGame;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityBetween extends ActivityTemplate{
	private static final String TAG = ActivityBetween.class.toString();
	// Font
    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private static final int[] GAMEINSTRUCTIONS = {R.id.tiltImgG,R.id.shakeImg,R.id.swipelaImg,R.id.taplaImg};
	
	private ArrayList<Class<?>> gamesList = new ArrayList<Class<?>>();
	private ArrayList<Integer> instructionsList = new ArrayList<Integer>();
	private int nextGame = 0;

	private int timeCounter = 3;
	private TextView timerField;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_between);
	    tf = Typeface.createFromAsset(getAssets(), FONTPATH);
	    
		setScoreView((TextView) findViewById(R.id.ScoreBetween), getAssets());
		setHeartsView((ImageView) findViewById(R.id.hearts));
	    
	    addGames();

	    if(!gamesList.isEmpty()){
		    nextGame = new Random().nextInt(gamesList.size());
		    // set instructionimage
		    ImageView instructions = (ImageView) findViewById(GAMEINSTRUCTIONS[instructionsList.get(nextGame)]);
			instructions.setVisibility(View.VISIBLE);
			
			timerField = (TextView) findViewById(R.id.timerBoxb);
			timerField.setTypeface(tf);
			
			startTimer();
			
			if(Settings.getLives() < 1){
		    	Log.i(TAG, "Game end, out of lives!");
		    	Intent endScreen = new Intent(this,ActivityEndScreen.class);
		    	if(endScreen != null){
		    		t.cancel();
		    		startActivity(endScreen);
		    		this.finish();
		    	}
		    }
	    }
	}
	private void startTimer(){
		t.scheduleAtFixedRate(new TimerTask(){
	        @Override
	        public void run() {
	            runOnUiThread(new Runnable(){
	                public void run(){
	                    timerField.setText(String.valueOf(timeCounter)); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    if(active){
		                    timeCounter--;
		                    if (timeCounter < 0){
		                    	timeCounter = 3;
		                    	if(!gamesList.isEmpty()){
		                    		nextGame();
		                    	}
		                    	t.cancel();
		                    }
	                    }
	                }
	            });
	
	        }
	    }, 0, 1000);
	}
	
	private void addGames(){	
		// ADD NEW GAMES HERE COMMENT THE GAMES YOU DONT WANT TO LOAD
	    // Add games: first gamevariable then instructions int (see gameInstructions)
	    checkDone(Accelerometer.class, 1);
	    checkDone(Accelerometer2.class, 1);
	    checkDone(GyroscopeGame.class, 0);
	    checkDone(Hangman.class, 3);
		checkDone(MemoryGame.class, 3);
	    checkDone(QuestionGamePicture.class, 3);
		checkDone(Quiz.class, 3);
	    checkDone(Shootgame.class, 3);
	    checkDone(SwipeGame1.class, 2);
	    checkDone(TargetGame.class, 3);
	    checkDone(TurnGame.class, 3);
	    
	    if(gamesList.isEmpty()){
	    	Settings.clearGamesDone();
	    	int difficulty = Settings.getDifficulty();
	    	if(difficulty < 3){
	    		Log.i(TAG, "Next difficulty");
	    		Settings.setDifficulty(difficulty + 1);
	    		addGames();
	    		Log.i(TAG, gamesList.toString());
	    	}else{
	    		t.cancel();
	    		Log.i(TAG, "Einde spel");
	    		Intent endScreen = new Intent(this,ActivityEndScreen.class);
	    		if(endScreen != null){
	    			startActivity(endScreen);
	    			this.finish();
	    		}
	    	}
	    }
	}
	// Check if game is already played this difficulty
	private void checkDone(Class<?> game, int instruction){
		ArrayList<Class<?>> gamesDone = Settings.getGamesDone();
		if(!gamesDone.contains(game)){
			gamesList.add(game);
			instructionsList.add(instruction);
		}
	}
	
	// Initiate next game
	private void nextGame(){
		Settings.addGameGamesDone(gamesList.get(nextGame));
		Intent minigame = new Intent(this,gamesList.get(nextGame));
		if(minigame != null){
			startActivity(minigame);
			this.finish();
		}
	}
	
	@Override
	public void onBackPressed(){
		Settings.resetAll();
		this.finish();
	}
}

package nl.hr.impossibleapp;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import nl.hr.impossibleapp.Gyrogame.GyroscopeGame;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_Between extends Activity{
	private static final String TAG = Activity_Between.class.toString();
	
	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private static final int[] gameInstructions = {R.id.tiltImgG,R.id.shakeImg,R.id.swipelaImg,R.id.taplaImg};
	
	private ArrayList<Class<?>> gamesList = new ArrayList<Class<?>>();
	private ArrayList<Integer> instructionsList = new ArrayList<Integer>();
	private int nextGame = 0;
	
	//Timer
	private Timer t = new Timer();
	private int TimeCounter = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_between);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    
	    addGames();
	    Score();
	    // if gameslist not empty, start timer
	    if(!gamesList.isEmpty()){
		    nextGame = new Random().nextInt(gamesList.size());
		    
		    // Set instructionview
		    ImageView instructions = (ImageView) findViewById(gameInstructions[instructionsList.get(nextGame)]);
			instructions.setVisibility(View.VISIBLE);
			
			// Textfield from xml
			final TextView timerField = (TextView) findViewById(R.id.timerBoxb);
			timerField.setTypeface(tf);
			
			
			t.scheduleAtFixedRate(new TimerTask() 
		    {
		        @Override
		        public void run() {
		            runOnUiThread(new Runnable() 
		            {
		                public void run() 
		                {
		                    timerField.setText(String.valueOf(TimeCounter)); // you can set it to a textView to show it to the user to see the time passing while he is writing.
		                    TimeCounter--;
		                    if (TimeCounter < 0)
		                    {
		                    	TimeCounter = 3;
		                    	if(!gamesList.isEmpty()){
		                    		nextGame();
		                    	}
		                    	t.cancel();
		                    }
		                }
		            });
		
		        }
		    }, 0, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
			
			if(Settings.getLives() < 1){
		    	Log.d(TAG, "Game end, out of lives!");
		    	Intent endScreen = new Intent(this,Activity_EndScreen.class);
		    	if(endScreen != null){
			    	//settings.resetAll();
		    		t.cancel();
		    		startActivity(endScreen);
		    		this.finish();
		    	}
		    }
	    }
	}

	public void addGames(){	
		// ADD NEW GAMES HERE COMMENT THE GAMES YOU DONT WANT TO LOAD
	    // Add games: first gamevariable then instructions int (see gameInstructions)
	    checkDone(GyroscopeGame.class, 0); //goed++
	    checkDone(Accelerometer2.class, 1); //goed++
	    checkDone(SwipeGame1.class, 2); //goed++
	    checkDone(TurnGame.class, 3); //goed++
	    checkDone(Hangman.class, 3); //goed++
	    checkDone(Accelerometer.class, 1); //goed++
	    checkDone(TargetGame.class, 3); //goed++
	    checkDone(Shootgame.class, 3); //goed++
	    
	    if(gamesList.isEmpty()){
	    	Settings.clearGamesDone();
	    	int difficulty = Settings.getDifficulty();
	    	if(difficulty < 3){
	    		Log.d(TAG, "Next difficulty");
	    		Settings.setDifficulty(difficulty + 1);
	    		addGames();
	    	}else{
	    		t.cancel();
	    		System.out.println("Einde spel");
	    		Intent endScreen = new Intent(this,Activity_EndScreen.class);
	    		if(endScreen != null){
	    			//settings.resetAll();
	    			startActivity(endScreen);
	    			this.finish();
	    		}
	    	}
	    }
	}
	// Check if game is already played this difficulty
	public void checkDone(Class<?> game, int instruction){
		ArrayList<Class<?>> gamesDone = Settings.getGamesDone();
		if(!gamesDone.contains(game)){
			gamesList.add(game);
			instructionsList.add(instruction);
		}
	}
	
	// Initiate next game
	public void nextGame(){
		Settings.addGameGamesDone(gamesList.get(nextGame));
		Intent game_page = new Intent(this,gamesList.get(nextGame));
		if(game_page != null){
			startActivity(game_page);
			this.finish();
		}
	}
	
	public void Score(){
		// show score
		int Score = Settings.getScore();
		TextView ScoreText = (TextView) findViewById(R.id.ScoreBetween);
		ScoreText.setTypeface(tf);
		ScoreText.setText("Score:"+Score);
	}
}

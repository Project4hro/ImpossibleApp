package nl.hr.impossibleapp.activities;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
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
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityBetween extends Activity{
	private static final String TAG = ActivityBetween.class.toString();
	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private static final int[] gameInstructions = {R.id.tiltImgG,R.id.shakeImg,R.id.swipelaImg,R.id.taplaImg};
	
	private ArrayList<Class<?>> gamesList = new ArrayList<Class<?>>();
	private ArrayList<Integer> instructionsList = new ArrayList<Integer>();
	private int nextGame = 0;

	private Timer t = new Timer();
	private int timeCounter = 3;
	private TextView timerField;
	
	private boolean active = false;
	
	@Override
    public void onStart(){
       super.onStart();
       active = true;
    } 

    @Override
    public void onStop(){
        super.onStop();
        active = false;
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_between);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    if(Settings.getName() == null){
	    	
	    }
	    addGames();
	    setHeartsScore();

	    if(!gamesList.isEmpty()){
		    nextGame = new Random().nextInt(gamesList.size());
		    
		    ImageView instructions = (ImageView) findViewById(gameInstructions[instructionsList.get(nextGame)]);
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
		Intent game_page = new Intent(this,gamesList.get(nextGame));
		if(game_page != null){
			Log.i(TAG, "Found new minigame");
			Log.i(TAG, game_page.toString());
			startActivity(game_page);
			Log.i(TAG, "started");
			this.finish();
		}
	}
	
	private void setHeartsScore(){
		TextView ScoreText = (TextView) findViewById(R.id.ScoreBetween);
		ScoreText.setTypeface(tf);
		ScoreText.setText("Score:"+Settings.getScore());
		ImageView heartsField = (ImageView) findViewById(R.id.hearts);
		int lives = Settings.getLives();
	    if(lives == 2){
	    	heartsField.setImageResource(R.drawable.heart2);
	    }else if(lives == 1){
	    	heartsField.setImageResource(R.drawable.heart1);
	    }else{
	    	heartsField.setImageResource(R.drawable.heart3);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
	    menu.add("Exit"); 
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
	    if (item.getTitle() == "Exit")
			t.cancel();
		    Settings.resetAll();
			this.finish();
	    return super.onOptionsItemSelected(item);    
	}
	
	@Override 
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public void onBackPressed(){
		Settings.resetAll();
		this.finish();
	}
}

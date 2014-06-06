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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_Between extends Activity
{ 
	GyroscopeGame gyroGame;
	
	int[] gameInstructions = {R.id.tiltImgG,R.id.shakeImg,R.id.swipelaImg,R.id.taplaImg};
	static boolean active = false;
	
	private Timer t = new Timer();
	private int TimeCounter = 3;
	int currentGame = 0;
	
	ArrayList<Class<?>> gamesList = new ArrayList<Class<?>>();
	ArrayList<Integer> gamesInstructions = new ArrayList<Integer>();
	
	int nextGame;
	
	@Override
	public void onStart() 
	{
	   super.onStart();
	   active = true;
	} 

    //OnPause function
    @Override
    public void onPause() //move to background, stop threads
    {
    	this.finish();
        super.onPause();
    }
	@Override
	public void onStop() 
	{
	   super.onStop();
	   //active = false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		active = true;
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_between);
	    
	    if(settings.getLives() < 1){
	    	System.out.println("Lives end:" + settings.getLives());
	    	settings.resetAll();
	    	Intent menu = new Intent(this,Activity_Menu.class);
	    	if(menu != null){
	    		startActivity(menu);
	    	}
	    }
	    if(settings.isNewGame()){
		    // put minigames in array
		    Class<?> gyroGame = GyroscopeGame.class;
		    Class<?> gyroGame2 = Accelerometer2.class;
		    Class<?> swipeGame = SwipeGame1.class;
		    Class<?> touchGame1 = TurnActivity.class;
		    
		    //Add minigames then instruction screen, comment the games you don't want to load
		    settings.addGameGamesList(gyroGame); //Add gyrogame
		    settings.addGameInstructions(0); //Add instruction screen from gameInstructions variable
		    settings.addGameGamesList(gyroGame2);
		    settings.addGameInstructions(1);
		    settings.addGameGamesList(swipeGame);
		    settings.addGameInstructions(2);
		    settings.addGameGamesList(touchGame1);
		    settings.addGameInstructions(3);
		    
		    settings.setNewGame(false);
		    settings.setDifficulty(1);
		    settings.setScore(0);
		    settings.setLives(3);
	    }
	    // check if all games are played once if yes increase difficulty and start all games again
	    if(settings.getGamesList().isEmpty()){
	    	int difficulty = settings.getDifficulty();
	    	
	    	if(difficulty != 3){
	    		settings.setDifficulty(difficulty+1);
		    	settings.setGamesList(settings.getGamesDone());
		    	settings.setGamesInstructions(settings.getGamesInstructionsDone());
		    	settings.resetDoneLists();
	    	}else{
	    		System.out.println("Afgelopen");
	    		settings.resetAll();
	    		Intent start_menu = new Intent(this,Activity_Menu.class);
	    		if(start_menu != null){
	    			startActivity(start_menu);
	    			this.finish();
	    		}
	    	}
	    }   
	    // Font path
	    String fontPath = "fonts/mvboli.ttf";
	    // Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		// Textfield from xml
		final TextView timerField = (TextView) findViewById(R.id.timerBoxb);
		timerField.setTypeface(tf);
		
		Score();
		
		gamesList.addAll(settings.getGamesList());
		gamesInstructions.addAll(settings.getGamesInstructions());
		if(!gamesList.isEmpty()){
			nextGame = new Random().nextInt(gamesList.size());
			final ImageView instructions = (ImageView) findViewById(gameInstructions[gamesInstructions.get(nextGame)]);
			instructions.setVisibility(View.VISIBLE);
		}
		
		
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
	                    if (TimeCounter < 1)
	                    {
	                    	TimeCounter = 3;
	                    	Goto_Game();
	                    	t.cancel();
	                    }
	                }
	            });

	        }
	    }, 0, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
	     
	}
	private void Goto_Game()
	{
		t.cancel();
		
		if(!gamesList.isEmpty()){
			Class<?> classToLoad = gamesList.get(nextGame);
			Intent game_page = new Intent(this,classToLoad);
		
		
			game_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			if(game_page != null)
			{
				if (active)
				{
					// add game to gamesdone, add instruction to done and delete from games yet to play
					settings.addGameGamesDone(classToLoad);
					settings.addInstructionsDone(gamesInstructions.get(nextGame));
					settings.removeGamesList(classToLoad);
					settings.removeGameInstructions(gamesInstructions.get(nextGame));
					
					System.out.println("Gameslist:" + settings.getGamesList());
					System.out.println("Gamesdone:" + settings.getGamesDone());
					System.out.println("Gamesinstructions:" + settings.getGamesInstructions());
					System.out.println("Difficulty:" + settings.getDifficulty());
					System.out.println("Lives:" + settings.getLives());
					
					startActivity(game_page);	
				}else{
					System.out.println("not active");
					active = true;
				}
				
			}
		}
		finish();
	}
	
	public void Score(){
		// show score
		int Score = settings.getScore();
		TextView ScoreText = (TextView) findViewById(R.id.ScoreBetween);
		ScoreText.setText("Score:"+Score);
		
	}
	//Eventlistener that checks if menu button is pressed
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// adds exit button
	    menu.add("Exit"); 
	    return super.onCreateOptionsMenu(menu);
	}
	//Eventlistener that checks if user presses exit
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // If exit    
	    if (item.getTitle() == "Exit") //user clicked Exit
			t.cancel();
	    	this.finish(); //will call onPause
	    return super.onOptionsItemSelected(item);    
	}
	@Override
	public void onBackPressed() {}
}


	
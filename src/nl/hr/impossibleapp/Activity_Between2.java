package nl.hr.impossibleapp;

import java.util.ArrayList;
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

public class Activity_Between2 extends Activity
{ 
	GyroscopeGame gyroGame;
	
	int[] gameInstructions = {R.id.tiltImgG,R.id.shakeImg,R.id.swipelaImg,R.id.taplaImg};
	static boolean active = false;
	
	private Timer t = new Timer();
	private int TimeCounter = 3;
	int currentGame = 0;
	
	ArrayList<Class<?>> list = new ArrayList<Class<?>>();
	
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
	   active = false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_between);
	    Score();
	    
	    // put minigames in array
	    Class<?> gyroGame = GyroscopeGame.class;
	    Class<?> gyroGame2 = Accelerometer2.class;
	    Class<?> swipeGame = SwipeGame1.class;
	    Class<?> touchGame1 = TurnActivity.class;
	    list.add(gyroGame);
	    list.add(gyroGame2);
	    list.add(swipeGame);
	    list.add(touchGame1);
	    
	    // Font path
	    String fontPath = "fonts/mvboli.ttf";
	    // Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		// Textfield from xml
		final TextView timerField = (TextView) findViewById(R.id.timerBoxb);
		timerField.setTypeface(tf);

		// check currentGame
		Intent mIntent = getIntent();
		currentGame = mIntent.getIntExtra("intCurrentGame", 0);
		System.out.println("Currentgame number = "+currentGame);
		final ImageView instructions = (ImageView) findViewById(gameInstructions[currentGame]);
		//final ImageView instructions = (ImageView) findViewById(gameInstructions[1]);
		instructions.setVisibility(View.VISIBLE);
		
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
	                    }
	                }
	            });

	        }
	    }, 0, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
	        
	}
	private void Goto_Game()
	{
		t.cancel();
		// check currentGame
		Intent mIntent = getIntent();
		int currentGame = mIntent.getIntExtra("intCurrentGame", 0);
		Class<?> classToLoad = list.get(currentGame);
		Intent game_page = new Intent(this,classToLoad);
		//Intent game_page = new Intent(this,Accelerometer2.class);
		currentGame ++;
		game_page.putExtra("intCurrentGame", currentGame);
		game_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(game_page != null)
		{
			if (active)
			{
				startActivity(game_page);	
			}
		}
		finish();
	}
	
	public void Score(){
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


	
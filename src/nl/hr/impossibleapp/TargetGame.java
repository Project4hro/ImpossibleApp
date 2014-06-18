package nl.hr.impossibleapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Configuration;

public class TargetGame extends Activity 
{	
	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	// Target image
	private ImageView img_target;
	private int countdown = 1;
	private int hits = 20;
    private TextView counterField;
	
	// Check if activity is running
	static boolean active = false;
    
	//Textview for timer
    private TextView timerField;
	private Timer t = new Timer();
	private int timeCounter = 15;
	
	private boolean beforeFirstHit = true;
    @Override
    public void onStart() 
    {
       super.onStart();
       active = true;
    } 

    @Override
    public void onStop() 
    {
        super.onStop();
        active = false;
    }
    //OnPause function
    @Override
    public void onPause() //move to background, stop threads
    {
        super.onPause();
    	this.finish();
    }
	//@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_targetgame);

	    //view reference to the app
	    final RelativeLayout mainView = (android.widget.RelativeLayout)findViewById(R.id.targetgame_view);
	    
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
		// Textfield from xml
		timerField = (TextView) findViewById(R.id.timerBox);
		timerField.setTypeface(tf);
		counterField = (TextView)findViewById(R.id.hitBox);
		counterField.setTypeface(tf);
    	hits --;
		ImageView heartsField = (ImageView) findViewById(R.id.hearts);
		int lives = Settings.getLives();
	    if(lives == 2){
	    	heartsField.setImageResource(R.drawable.heart2);
	    }else if(lives == 1){
	    	heartsField.setImageResource(R.drawable.heart1);
	    }
	    TextView scoreText = (TextView) findViewById(R.id.scoreBox);
	    scoreText.setText("Score: " + Settings.getScore());

		img_target = (ImageView) findViewById(R.id.targetButton);

		img_target.setOnClickListener(new android.view.View.OnClickListener() 
		{
		    public void onClick (View v) 
		    {
		    	moveImg(); 
		    	if (!beforeFirstHit)
		    	{
					Sound.correct(getBaseContext());
		    	}
		    	beforeFirstHit = false;
		    	hits --;
		    	counterField.setText(String.valueOf("Raak er: "+hits)); 

				if (hits == 0)
				{
					Sound.stopCountDown(getBaseContext());
			    	Sound.wonMinigame(getBaseContext());
		    		int score = Settings.getScore() + timeCounter;
		            Settings.setScore(score);
		        	betweenScreen();
		        	t.cancel();
				}
		    }   
		});
		img_target.performClick(); // run once at the start to set image and position randomly*/
	    
		//touch event that checks on user touch
		mainView.setOnTouchListener(new android.view.View.OnTouchListener() 
	  	{
			public boolean onTouch(android.view.View v, android.view.MotionEvent e)
	  	    {
				Sound.wrong(getBaseContext());
		    	moveImg(); 
				return true;
	  	}}); 
	}
    private void moveImg()
    {	
    	final Random rand = new Random();
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = rand.nextInt((int) (metrics.widthPixels - img_target.getWidth()*1.5));
        int height = rand.nextInt((int) (metrics.heightPixels - img_target.getHeight()*1.5));

        final RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        flp.setMargins(width, height, 0, 0);
        img_target.setLayoutParams(flp);    
    	countdown = 2;  
    }
	@Override
	protected void onResume()
	{ 	
	    t.scheduleAtFixedRate(new TimerTask() 
	    {
	        @Override
	        public void run() {
	            runOnUiThread(new Runnable() 
	            {
	                public void run() 
	                {
	                	timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    
	                    timeCounter--;
	                    countdown --;
	    	            if (countdown== 0)
	    	            {
	    	            	moveImg();
	    	            }
	                    if(timeCounter == 4){
	                    	Sound.countDown(getBaseContext());
	                    }
	                    if (timeCounter < 0)
	                    {
	                    	Sound.gameOver(getBaseContext());
	                    	Settings.setLives(Settings.getLives() - 1);
	                    	betweenScreen();
	                    	t.cancel();
	                    }
	                }
	            });
	        }
	    }, 0, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
	    super.onResume();
	} // onResume
	private void betweenScreen()
	{
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent between_page = new Intent(this, ActivityBetween.class);
		if(between_page != null)
		{
			if (active)
			{
				startActivity(between_page);
			}	
		}
		this.finish();
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
		    Settings.resetAll();
			this.finish();
	    return super.onOptionsItemSelected(item);    
	}
	//listener for config change, so it stays in landscape mode
	@Override 
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public void onBackPressed(){}
}


	
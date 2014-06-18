package nl.hr.impossibleapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import nl.hr.impossibleapp.activities.ActivityMenu;

public class QuestionGamePicture extends Activity
{
	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	static boolean active = false;
	

    private TextView timerBox;

    private Timer t;
    private int TimeCounter = 7;
    
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.layout_question_game_pic);
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        
        ImageView heartsField = (ImageView) findViewById(R.id.hearts);
        int lives = Settings.getLives();
        if(lives == 2){
        	heartsField.setImageResource(R.drawable.heart2);
        }else if(lives == 1){
        	heartsField.setImageResource(R.drawable.heart1);
        }else{
        	heartsField.setImageResource(R.drawable.heart3);
        }
		
		timerBox = (TextView)findViewById(R.id.timerBox);
		timerBox.setTypeface(tf);
		TextView scoreText = (TextView) findViewById(R.id.scoreBox);
		scoreText.setTypeface(tf);
	    scoreText.setText("Score: " + Settings.getScore());
	    
		t = new Timer();
	    t.scheduleAtFixedRate(new TimerTask() 
	    {
	        @Override
	        public void run() {
	            
	       	runOnUiThread(new Runnable() 
	            {
	                public void run() 
	                {
	                	timerBox.setText(getResources().getString(R.string.time) + ": " + String.valueOf(TimeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    TimeCounter--;
	                    
	                    if(TimeCounter  == 2)
	                    {
	                    	Sound.shortCountDown(getBaseContext());
	                    }
	                    if (TimeCounter < 0)
	                    {
	                    	System.out.println("[QuestionGamePicture] Minus life: out of time, " + Settings.getLives());
	                    	TimeCounter = 10;
	                    	betweenScreen();
	                    	t.cancel();
	                    }
	                 }
	            });
	        }
	    }, 0, 1000);
    }
    
    protected void onStart()
    {
    	super.onStart();
        active = true;
    }
    
    public void onStop() 
    {
        super.onStop();
        active = false;
    }
    
    protected void onResume() 
    {
        super.onResume();
    }
    
    public void betweenScreen()
	{
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent between_page = new Intent(this, QuestionGame.class);
		between_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		int score = Settings.getScore() + TimeCounter;
        Settings.setScore(score);
		if(between_page != null)
		{
			if (active)
			{
				startActivity(between_page);
			}	
		}
		finish();
	}

    protected void onPause() 
    {
        super.onPause();
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
		    Intent menu_page = new Intent(this, ActivityMenu.class);
			if(menu_page != null){
  				Settings.resetAll();
				startActivity(menu_page);
				this.finish();
			}
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

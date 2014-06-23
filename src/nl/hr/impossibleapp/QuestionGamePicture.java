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

public class QuestionGamePicture extends Activity{
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private boolean active = false;
	
    private TextView timerBox;

    private Timer t = new Timer();
    private int timeCounter = 7;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.layout_question_game_pic);
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        int difficulty = Settings.getDifficulty();
        if(difficulty == 3){
        	timeCounter = 3;
        }else if(difficulty == 2){
        	timeCounter = 5; 
        }
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
	    
	    startTimer();
    }
    
    protected void onStart(){
    	super.onStart();
        active = true;
    }
    
    public void onStop(){
        super.onStop();
        active = false;
    }

    private void startTimer(){
    	t.scheduleAtFixedRate(new TimerTask(){
	        @Override
	        public void run() {
	            
	       	runOnUiThread(new Runnable(){
	                public void run(){
	                	timerBox.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    if(active){
		                	timeCounter--;
		                    if(timeCounter  == 2){
		                    	Sound.shortCountDown(getBaseContext());
		                    }
		                    if (timeCounter < 0){
		                    	betweenScreen();
		                    	t.cancel();
		                    }
	                    }
	                 }
	            });
	        }
	    }, 0, 1000);
    }
    private void betweenScreen(){
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent questionGame = new Intent(this, QuestionGame.class);
		questionGame.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(questionGame != null){
			if (active){
				startActivity(questionGame);
				this.finish();
			}	
		}
	}

    /*protected void onPause(){
        super.onPause();
    }*/

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
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public void onBackPressed(){}
}

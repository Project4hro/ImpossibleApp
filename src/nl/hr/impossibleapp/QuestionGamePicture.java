package nl.hr.impossibleapp;

import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import nl.hr.impossibleapp.activities.ActivityTemplate;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;

public class QuestionGamePicture extends ActivityTemplate{
    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
	
    private TextView timerBox;
    private TextView scoreView;
	private ImageView heartsView;

    private int timeCounter = 7;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.layout_question_game_pic);
        tf = Typeface.createFromAsset(getAssets(), FONTPATH);
        int difficulty = Settings.getDifficulty();
        if(difficulty == 3){
        	timeCounter = 3;
        }else if(difficulty == 2){
        	timeCounter = 5; 
        }
        heartsView = (ImageView) findViewById(R.id.hearts);
        setHeartsView(heartsView);
		
		timerBox = (TextView)findViewById(R.id.timerBox);
		timerBox.setTypeface(tf);
		scoreView = (TextView) findViewById(R.id.scoreBox);
		setScoreView(scoreView, getAssets());
	    
	    startTimer();
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
		if(questionGame != null && active){
			startActivity(questionGame);
			this.finish();
		}
	}
}

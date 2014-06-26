package nl.hr.impossibleapp;

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
import java.util.TimerTask;

import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.activities.ActivityTemplate;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;

public class TargetGame extends ActivityTemplate{	
    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private ImageView imgTarget;
	private int countdown = 1;
	private int hits = 20;
    private TextView counterField;
    
    private TextView timerField;
    private TextView scoreView;
	private ImageView heartsView;
	private int timeCounter = 25;
	
	private boolean beforeFirstHit = true;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_targetgame);

	    int difficulty = Settings.getDifficulty();
	    if(difficulty == 3){
	    	timeCounter = 15;
	    }else if(difficulty == 2){
	    	timeCounter = 20;
	    }
	    final RelativeLayout mainView = (android.widget.RelativeLayout)findViewById(R.id.targetgame_view);
	    
	    tf = Typeface.createFromAsset(getAssets(), FONTPATH);

		timerField = (TextView) findViewById(R.id.timerBox);
		timerField.setTypeface(tf);
		counterField = (TextView)findViewById(R.id.hitBox);
		counterField.setTypeface(tf);
    	hits --;
		heartsView = (ImageView) findViewById(R.id.hearts);
		setHeartsView(heartsView);
		
	    scoreView = (TextView) findViewById(R.id.scoreBox);
	    setScoreView(scoreView, getAssets());

		imgTarget = (ImageView) findViewById(R.id.targetButton);

		imgTarget.setOnClickListener(new android.view.View.OnClickListener(){
		    public void onClick (View v) {
		    	moveImg(); 
		    	if (!beforeFirstHit){
					Sound.correct(getBaseContext());
		    	}
		    	beforeFirstHit = false;
		    	hits --;
		    	counterField.setText(String.valueOf("Raak er: "+hits)); 

				if (hits == 0){
					Sound.stopCountDown(getBaseContext());
			    	Sound.wonMinigame(getBaseContext());
		            Settings.addScore(timeCounter);
		        	betweenScreen();
		        	t.cancel();
				}
		    }   
		});
		imgTarget.performClick(); // run once at the start to set image and position randomly*/
	    
		mainView.setOnTouchListener(new android.view.View.OnTouchListener() {
			public boolean onTouch(android.view.View v, android.view.MotionEvent e){
				Sound.wrong(getBaseContext());
		    	moveImg(); 
				return true;
	  	}}); 
		startTimer();
	}
    private void moveImg(){	
    	// move target
    	final Random rand = new Random();
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = rand.nextInt((int) (metrics.widthPixels - imgTarget.getWidth()*1.5));
        int height = rand.nextInt((int) (metrics.heightPixels - imgTarget.getHeight()*1.5));

        final RelativeLayout.LayoutParams flp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        flp.setMargins(width, height, 0, 0);
        imgTarget.setLayoutParams(flp);    
    	countdown = 2;  
    }
	
	protected void startTimer(){ 	
	    t.scheduleAtFixedRate(new TimerTask(){
	        @Override
	        public void run() {
	            runOnUiThread(new Runnable(){
	                public void run(){
	                	timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    if(active){
		                    timeCounter--;
		                    countdown --;
		    	            if (countdown== 0){
		    	            	moveImg();
		    	            }
		                    if(timeCounter == 4){
		                    	Sound.countDown(getBaseContext());
		                    }
		                    if (timeCounter < 0){
		                    	Sound.gameOver(getBaseContext());
		                    	Settings.setLives(Settings.getLives() - 1);
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
		Intent betweenPage = new Intent(this, ActivityBetween.class);
		if(betweenPage != null && active){
			startActivity(betweenPage);
		}
		this.finish();
	}
}


	
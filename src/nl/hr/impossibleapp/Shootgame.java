package nl.hr.impossibleapp;

import java.util.Random;
import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.activities.ActivityTemplate;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Shootgame extends ActivityTemplate{
	private static final String TAG = Shootgame.class.toString();
    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
    
    private TextView timerField;
    private TextView scoreView;
	private ImageView heartsView;
    private int timeCounter = 20;

	private boolean win[] = {false,false,false};
	private ImageView logos[] = {null,null,null,null};
	
    private boolean end = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_shootgame1);
	    tf = Typeface.createFromAsset(getAssets(), FONTPATH);
	    
	    timerField = (TextView) findViewById(R.id.timerBox);
	    timerField.setTypeface(tf);
	    int difficulty = Settings.getDifficulty();
	    if(difficulty == 3){
	    	timeCounter = 10;
	    }else if(difficulty == 2){
	    	timeCounter = 15;
	    }
		heartsView = (ImageView) findViewById(R.id.hearts);
	    scoreView = (TextView) findViewById(R.id.scoreBox);
	    setHeartsView(heartsView);
	    setScoreView(scoreView, getAssets());
	    startTimer();
    }
    
    private void startTimer(){
    	t.scheduleAtFixedRate(new TimerTask(){
	    	@Override
	    	public void run() {
	    		runOnUiThread(new Runnable(){
	    			public void run(){
	    				timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	    				if(active){
		    				timeCounter--;
		    				move();
		    				
		    				if(timeCounter  == 4){
		                    	Sound.countDown(getBaseContext());
		                    }
		    				
		    				if (timeCounter < 0){
		    					Log.i(TAG, "Out of time");
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
    @Override
	public boolean onTouchEvent(MotionEvent event) {
	    int x = (int)event.getX() -40;
	    int y = (int)event.getY() -40;
	    ImageView gunsight = getgunsight();
	    switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	        	break;
	        case MotionEvent.ACTION_MOVE:
	        	gunsight.setX(x);
	    	    gunsight.setY(y);
	    	    break;
	        case MotionEvent.ACTION_UP:
	        	shoot(x+40,y+40);
	        	break;
	    }
	    return false;
	}
    
    private ImageView getgunsight(){
    	return (ImageView) findViewById(R.id.gunsight);
		
	}
    
    private void shoot(int x, int y){
		for (int logonumber = 0;logonumber < 3;logonumber++){
			logos[logonumber] = getlogo(logonumber);
				int logoLeft = (int) (logos[logonumber].getX()-5);
				int logoRight = (int) (logos[logonumber].getX()+45);
				int logoTop = (int) (logos[logonumber].getY()+5);
				int logoBottom  =(int) (logos[logonumber].getY()+35);
				if(x >= logoLeft && x <= logoRight && y >= logoTop && y <= logoBottom){
					logos[logonumber].setImageDrawable(null);
					win[logonumber] = true;
					Sound.correct(getBaseContext());
					checkwin();
				}
		} 
	}
    
    private void move(){
    	// move logos
		for (int logonumber = 0;logonumber < 3;logonumber++){
			logos[logonumber] = getlogo(logonumber);
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
			float newX = new Random().nextInt(width-60)+30;
			float newY = new Random().nextInt(height-60)+30;
			logos[logonumber].setX(newX);
			logos[logonumber].setY(newY);
		}
	}
    
    private ImageView getlogo(int logonumber){
		ImageView logo = null;
		switch(logonumber){
			case 0:
				logo = (ImageView) findViewById(R.id.logo0);
				break;
			case 1:
				logo = (ImageView) findViewById(R.id.logo1);
				break;
			case 2:
				logo = (ImageView) findViewById(R.id.logo2);
				break;
		}
		return logo;
	}
    
    private void checkwin(){
		if (win[0] == true && win[1] == true && win[2] == true && end){
	        Settings.addScore(timeCounter);
	        betweenScreen();
	    	Sound.wonMinigame(getBaseContext());
	        t.cancel();
	        end = false;
		}
	}
    
    private void betweenScreen(){
		t.cancel();
		Intent betweenPage = new Intent(this, ActivityBetween.class);
		Sound.stopCountDown(getBaseContext());
		betweenPage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(betweenPage != null && active){
			startActivity(betweenPage);
		}
		this.finish();
	}
}

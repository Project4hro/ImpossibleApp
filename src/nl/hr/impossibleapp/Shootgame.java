package nl.hr.impossibleapp;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Shootgame extends Activity{
	private static final String TAG = Shootgame.class.toString();
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
    private TextView timerField;
    private Timer t = new Timer();
    private int timeCounter = 20;

	private boolean win[] = {false,false,false};
	private ImageView logos[] = {null,null,null,null};
	
    private boolean active = false;
    private boolean end = true;
    
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
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_shootgame1);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    
	    timerField = (TextView) findViewById(R.id.timerBox);
	    timerField.setTypeface(tf);
	    int difficulty = Settings.getDifficulty();
	    if(difficulty == 3){
	    	timeCounter = 10;
	    }else if(difficulty == 2){
	    	timeCounter = 15;
	    }
	    
	    startTimer();
    }
    
    private void startTimer(){
    	t.scheduleAtFixedRate(new TimerTask() 
	    {
	    	@Override
	    	public void run() {
	    		runOnUiThread(new Runnable(){
	    			public void run(){
	    				timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	    				if(active){
		    				timeCounter--;
		    				move();
		    				UpdateScreen();
		    				
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
	        case MotionEvent.ACTION_MOVE:
	        	gunsight.setX(x);
	    	    gunsight.setY(y);
	        case MotionEvent.ACTION_UP:
	        	shoot(x+40,y+40);
	    }
	    return false;
	}
    
    private ImageView getgunsight(){
    	return (ImageView) findViewById(R.id.gunsight);
		
	}
    
    private void shoot(int X, int Y){
		for (int logonumber = 0;logonumber < 3;logonumber++){
			logos[logonumber] = getlogo(logonumber);
				int Logoleft = (int) (logos[logonumber].getX()-5);
				int Logoright = (int) (logos[logonumber].getX()+45);
				int Logotop = (int) (logos[logonumber].getY()+5);
				int Logobottom  =(int) (logos[logonumber].getY()+35);
				if(X >= Logoleft && X <= Logoright && Y >= Logotop && Y <= Logobottom){
					logos[logonumber].setImageDrawable(null);
					win[logonumber] = true;
					Sound.correct(getBaseContext());
					checkwin();
				}
		} 
	}
	
    private void UpdateScreen(){
	    int lives = Settings.getLives();
		ImageView heartsField = (ImageView) findViewById(R.id.hearts);
	    if(lives == 2){
	    	heartsField.setImageResource(R.drawable.heart2);
	    }else if(lives == 1){
	    	heartsField.setImageResource(R.drawable.heart1);
	    }else{
	    	heartsField.setImageResource(R.drawable.heart3);
	    }
	    TextView scoreText = (TextView) findViewById(R.id.scoreBox);
	    scoreText.setTypeface(tf);
	    scoreText.setText("Score: " + Settings.getScore());
	    
	}
    
    private void move(){
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
		Intent between_page = new Intent(this, ActivityBetween.class);
		Sound.stopCountDown(getBaseContext());
		between_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(between_page != null){
			if (active){
				startActivity(between_page);
			}	
		}
		this.finish();
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
  	public void onBackPressed(){}
}

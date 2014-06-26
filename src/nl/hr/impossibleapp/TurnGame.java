	 package nl.hr.impossibleapp;

import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.activities.ActivityTemplate;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class TurnGame extends ActivityTemplate {
    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
    
    private float[] currentRotation = {0,0,0,0,0,0,0,0,0,0,0,0};
	private ImageView img;
	private int win = 3;

    int timeCounter = 12;
    TextView timerField;
    private TextView scoreView;
	private ImageView heartsView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_turngame);
	    tf = Typeface.createFromAsset(getAssets(), FONTPATH);
	    timerField = (TextView) findViewById(R.id.timerBox);
	    timerField.setTypeface(tf);
	    int difficulty = Settings.getDifficulty();
	    if(difficulty == 3){
	    	timeCounter = 6;
	    }else if(difficulty == 2){
	    	timeCounter = 9;
	    }
	    heartsView = (ImageView) findViewById(R.id.hearts);
	    scoreView = (TextView) findViewById(R.id.scoreBox);
	    setHeartsView(heartsView);
	    setScoreView(scoreView, getAssets());
	    
	    t.scheduleAtFixedRate(new TimerTask(){
	    	@Override
	    	public void run() {
	    		runOnUiThread(new Runnable(){
	    			public void run(){
	    				timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	    				if(active){
	    					timeCounter--;
	    					
	    					if(timeCounter  == 4){
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
	
	public void turnImg2(View v) {
		img = (ImageView) findViewById(R.id.imageView2);
		makeTurn(2);
	}
	public void turnImg3(View v) {
		img = (ImageView) findViewById(R.id.imageView3);
		makeTurn(3);
	}
	public void turnImg4(View v) {
		img = (ImageView) findViewById(R.id.imageView4);
		makeTurn(4);
	}
	public void turnImg5(View v) {
		img = (ImageView) findViewById(R.id.imageView5);
		makeTurn(5);
	}
	public void turnImg6(View v) {
		img = (ImageView) findViewById(R.id.imageView6);
		makeTurn(6);
	}
	public void turnImg7(View v) {
		img = (ImageView) findViewById(R.id.imageView7);
		makeTurn(7);
	}
	public void turnImg8(View v) {
		img = (ImageView) findViewById(R.id.imageView8);
		makeTurn(8);
	}
	public void turnImg9(View v) {
		img = (ImageView) findViewById(R.id.imageView9);
		makeTurn(9);
	}
	public void turnImg10(View v) {
		img = (ImageView) findViewById(R.id.imageView10);
		makeTurn(10);
	}
	public void turnImg11(View v) {
		img = (ImageView) findViewById(R.id.imageView11);
		makeTurn(11);
	}
	
	public void win(){
		if (win == 10){
			Sound.wonMinigame(getBaseContext());
			betweenScreen();
			
		}
	}
	// turn image
	private void makeTurn(int id){
		RotateAnimation anim = new RotateAnimation(currentRotation[id], currentRotation[id] + 90,
	            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
	    currentRotation[id] = (currentRotation[id] + 90) % 360;
	    anim.setInterpolator(new LinearInterpolator());
	    anim.setDuration(100);
	    anim.setFillEnabled(true);
	    anim.setFillAfter(true);
	    img.startAnimation(anim);
		
	    changeturns(id, (int) currentRotation[id]);
	    win();
	}

	private void changeturns(int id, int currentRotation2) {
		if(currentRotation2 == 90){
			if (id == 2 || id == 3 || id == 5 || id == 7 || id == 9 || id == 10 ){
				win++;
			}else if(id == 4 || id == 8){
				// curves, do nothing
			}else{
				win--;
			}
		}else if (currentRotation2 == 180){
			if (id == 6 || id == 11){
				win++;
			}else if(id == 4 || id == 5 || id == 9){
				// curves, do nothing
			}else{
				win--;
			}
		}else if (currentRotation2 == 270){
			if (id == 2 || id == 3 || id == 4 || id == 7 || id == 10 ){
				win++;
			}else if(id == 5 || id == 8 || id == 9){
				// curves, do nothing
			}else{
				win--;
			}
		}else if (currentRotation2 == 0){
			if (id == 6 || id == 8 || id == 11){
				win +=1;
			}else{
				win--;
			}
		}
	}
	
	private void betweenScreen(){
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent betweenPage = new Intent(this, ActivityBetween.class);
        Settings.addScore(timeCounter);
		if(betweenPage != null && active){
			startActivity(betweenPage);
			this.finish();
		}
	}
}

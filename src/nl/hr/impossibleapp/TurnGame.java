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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class TurnGame extends Activity{
	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
    private float currentRotation[] = {0,0,0,0,0,0,0,0,0,0,0,0};
	private ImageView img;
	private int win = 3;
    Timer t;
    int TimeCounter = 20;
    TextView timerField;
    
	// Check if activity is running
    private static boolean active = false; 
	
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
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_turngame);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    timerField = (TextView) findViewById(R.id.timerBox);
	    timerField.setTypeface(tf);
	    
	    UpdateScreen();
	    
	    t = new Timer();
	    t.scheduleAtFixedRate(new TimerTask(){
	    	@Override
	    	public void run() {
	    		runOnUiThread(new Runnable(){
	    			public void run(){
	    				timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(TimeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	    				TimeCounter--;
	    				
	    				if(TimeCounter  == 4){
	                    	Sound.countDown(getBaseContext());
	                    }
	    				
	    				if (TimeCounter < 0){
	    					Sound.gameOver(getBaseContext());
	                    	Settings.setLives(Settings.getLives() - 1);
	                    	System.out.println("[TurnGame] Minus life: out of time, " + Settings.getLives());
	    					betweenScreen();
	    					t.cancel();
                     	}
                 	}
	    		});

	    	}
	     }, 0, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
	} //OnCreate
	
	public void Turn_Img2(View v) {
		img = (ImageView) findViewById(R.id.imageView2);
		Make_Turn(2);
	}
	public void Turn_Img3(View v) {
		img = (ImageView) findViewById(R.id.imageView3);
		Make_Turn(3);
	}
	public void Turn_Img4(View v) {
		img = (ImageView) findViewById(R.id.imageView4);
		Make_Turn(4);
	}
	public void Turn_Img5(View v) {
		img = (ImageView) findViewById(R.id.imageView5);
		Make_Turn(5);
	}
	public void Turn_Img6(View v) {
		img = (ImageView) findViewById(R.id.imageView6);
		Make_Turn(6);
	}
	public void Turn_Img7(View v) {
		img = (ImageView) findViewById(R.id.imageView7);
		Make_Turn(7);
	}
	public void Turn_Img8(View v) {
		img = (ImageView) findViewById(R.id.imageView8);
		Make_Turn(8);
	}
	public void Turn_Img9(View v) {
		img = (ImageView) findViewById(R.id.imageView9);
		Make_Turn(9);
	}
	public void Turn_Img10(View v) {
		img = (ImageView) findViewById(R.id.imageView10);
		Make_Turn(10);
	}
	public void Turn_Img11(View v) {
		img = (ImageView) findViewById(R.id.imageView11);
		Make_Turn(11);
	}
	
	public void win(){
		if (win == 10){
			Sound.wonMinigame(getBaseContext());
			betweenScreen();
			
		}
	}
	
	public void Make_Turn(int id){
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
			}else if(id == 4 || id == 8){}else{
				win--;
			}
		}else if (currentRotation2 == 180){
			if (id == 6 || id == 11){
				win++;
			}else if(id == 4 || id == 5 || id == 9){}else{
				win--;
			}
		}else if (currentRotation2 == 270){
			if (id == 2 || id == 3 || id == 4 || id == 7 || id == 10 ){
				win++;
			}else if(id == 5 || id == 8 || id == 9){}else{
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
	
	public void UpdateScreen(){
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
	
	public void betweenScreen(){
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent between_page = new Intent(this, Activity_Between.class);
		int score = Settings.getScore() + TimeCounter;
        Settings.setScore(score);
		if(between_page != null){
			if (active)
			{
				startActivity(between_page);
				this.finish();	
			}
		}
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
		    Intent menu_page = new Intent(this, Activity_Menu.class);
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
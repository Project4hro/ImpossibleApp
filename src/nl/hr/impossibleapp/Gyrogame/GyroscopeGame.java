package nl.hr.impossibleapp.gyrogame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorEventListener;

public class GyroscopeGame extends Activity 
{
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
    private boolean addedOnce = false;

	private boolean up = true;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	private int countdown = 1;
	
	boolean active = false;

	BallScript myBall = null;
    Handler RedrawHandler = new Handler(); 
    Timer ballTimer = null;
    TimerTask ballTask = null;
    int screenWidth, screenHeight;
    android.graphics.PointF ballPos, ballSpeed, squarePos;
    
    SquareScript mySquareU = null;
    SquareScript mySquareD = null;
    SquareScript mySquareL = null;
    SquareScript mySquareR = null;
    long millisUntilFinished = 12000;
    
    private TextView timerField;
	private Timer t = new Timer();
	private int timeCounter = 12;
	
    @Override
    public void onStart() {
       super.onStart();
       active = true;
    } 

    @Override
    public void onStop() 
    {
        super.onStop();
        active = false;
    }
    
    @Override
    public void onPause()
    {
        ballTimer.cancel(); 
        ballTimer = null;
        ballTask = null;
        super.onPause();
    	this.finish();
    }
   
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_gyrogame);
	    
	    final FrameLayout mainView = (android.widget.FrameLayout)findViewById(R.id.gyrogame_view);
	    
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		int difficulty = Settings.getDifficulty();
		if(difficulty == 3){
			timeCounter = 8;
		}else if(difficulty == 2){
			timeCounter = 10;
		}
		timerField = (TextView) findViewById(R.id.timerBox);
		timerField.setTypeface(tf);
		ImageView heartsField = (ImageView) findViewById(R.id.hearts);
		int lives = Settings.getLives();
	    if(lives == 2){
	    	heartsField.setImageResource(R.drawable.heart2);
	    }else if(lives == 1){
	    	heartsField.setImageResource(R.drawable.heart1);
	    }
	    TextView scoreText = (TextView) findViewById(R.id.scoreBox);
	    scoreText.setTypeface(tf);
	    scoreText.setText("Score: " + Settings.getScore());
	    
		Display display = getWindowManager().getDefaultDisplay();  
		screenWidth = display.getWidth(); 
		screenHeight = display.getHeight();
		ballPos = new android.graphics.PointF();
		ballSpeed = new android.graphics.PointF();
		        
		ballPos.x = screenWidth/2; 
		ballPos.y = screenHeight/2; 
		ballSpeed.x = 0;
		ballSpeed.y = 0; 
		
		squarePos = new android.graphics.PointF();
		squarePos.x = screenWidth/4;
		squarePos.y = screenHeight/2;
		
		myBall = new BallScript(this, ballPos.x, ballPos.y, 10);
		mySquareU = new SquareScript(this, 0,0,screenWidth,screenHeight,true,false,false,false);
		mySquareD = new SquareScript(this, 0,0,screenWidth,screenHeight,false,true,false,false);
		mySquareL = new SquareScript(this, 0,0,screenWidth,screenHeight,false,false,true,false);
		mySquareR = new SquareScript(this, 0,0,screenWidth,screenHeight,false,false,false,true);
		
		mySquareU.setVisibility(View.VISIBLE);
		mySquareD.setVisibility(View.INVISIBLE);
		mySquareL.setVisibility(View.INVISIBLE);	
		mySquareR.setVisibility(View.INVISIBLE);
		
		mainView.addView(mySquareU);
		mainView.addView(mySquareD);
		mainView.addView(mySquareL);
		mainView.addView(mySquareR);
		mainView.addView(myBall);

		myBall.invalidate(); 

		((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
		     new SensorEventListener() {    
		        @Override  
		        public void onSensorChanged(SensorEvent event) {  
		        	if(active){
						ballSpeed.y = event.values[0];
						ballSpeed.x = event.values[1];
		        	}
		        }
		        @Override  
		        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
		    },

		    ((SensorManager)getSystemService(Context.SENSOR_SERVICE))
		    .getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),   
		     SensorManager.SENSOR_DELAY_NORMAL);
		startTimer();
	}
	
	@Override
	protected void onResume()
	{ 	
	    ballTimer = new Timer(); 
	    ballTask = new TimerTask() {
	    public void run() {


	    android.util.Log.d("TiltBall","Timer Hit - " + ballPos.x + ":" + ballPos.y);


	    ballPos.x += ballSpeed.x;
	    ballPos.y += ballSpeed.y;


	    if (ballPos.x > screenWidth) ballPos.x=screenWidth;
	    if (ballPos.y > screenHeight) ballPos.y=screenHeight;
	    if (ballPos.x < 0) ballPos.x=(screenWidth-screenWidth);
	    if (ballPos.y < 0) ballPos.y=screenHeight-screenHeight;
	    
	    if (ballPos.x > (screenWidth/2 - 10) && ballPos.x < (screenWidth/2 + 10) && ballPos.y < 60 && up)
	    {
	    	if(!addedOnce){
		    	updateScore();
		    	Sound.wonMinigame(getBaseContext());
		    	betweenScreen();
		    	addedOnce = true;
	    	}
	    }
	    if (ballPos.x > (screenWidth/2 - 10) && ballPos.x < (screenWidth/2 + 10) && ballPos.y > 420 &&  down)
	    {
	    	if(!addedOnce){
		    	updateScore();
		    	Sound.wonMinigame(getBaseContext());
		    	betweenScreen();
		    	addedOnce = true;
	    	}
	    }
	    if (ballPos.x < 60 && ballPos.y < (screenHeight/2 + 5) && ballPos.y > (screenHeight/2 -5) && left)
	    {
	    	if(!addedOnce){
		    	updateScore();
		    	Sound.wonMinigame(getBaseContext());
		    	betweenScreen();
		    	addedOnce = true;
	    	}
	    }
	    if (ballPos.x > 790 && ballPos.y < (screenHeight/2 + 5) && ballPos.y > (screenHeight/2 -5) && right)
	    {
	    	if(!addedOnce){
		    	updateScore();
		    	Sound.wonMinigame(getBaseContext());
		    	betweenScreen();
		    	addedOnce = true;
	    	}
	    }
	    if(active){
		    myBall.x = ballPos.x;
		    myBall.y = ballPos.y;
	    }

	    RedrawHandler.post(new Runnable() {
	        public void run() {    
	        myBall.invalidate();
	        }});
	    }};
	    
	    ballTimer.schedule(ballTask,10,10);
	    super.onResume();
	}
	private void startTimer(){
		t.scheduleAtFixedRate(new TimerTask() 
	    {
	        @Override
	        public void run() {
	            runOnUiThread(new Runnable() 
	            {
	                public void run() 
	                {
	                    timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    if(active){
		                    timeCounter--;
		                    countdown --;
		                    
		                    if (countdown== 0){
		    	            	if (up){
		    	            		up = false;
		    	            		down = true;
		    	            		mySquareU.setVisibility(View.INVISIBLE);
		    	            		mySquareD.setVisibility(View.VISIBLE);
		    	            	}else if (down){
		    	            		down = false;
		    	            		left = true;
		    	            		mySquareD.setVisibility(View.INVISIBLE);
		    	            		mySquareL.setVisibility(View.VISIBLE);
		    	            	}else if (left){
		    	            		left = false;
		    	            		right = true;
		    	            		mySquareL.setVisibility(View.INVISIBLE);
		    	            		mySquareR.setVisibility(View.VISIBLE);
		    	            	}else if (right){
		    	            		right = false;
		    	            		up = true;
		    	            		mySquareR.setVisibility(View.INVISIBLE);
		    	            		mySquareU.setVisibility(View.VISIBLE);
		    	            	}
		    	            	countdown = 1;
		    	            }
		                    if(timeCounter == 4){
		                    	Sound.countDown(getBaseContext());
		                    }
		                    if (timeCounter < 0){
		                    	Sound.gameOver(getBaseContext());
		            	    	System.out.println("timelowerthen1");
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
	public void betweenScreen()
	{
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent between_page = new Intent(this, ActivityBetween.class);
		between_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		
		if(between_page != null)
		{
			if (active)
			{
				startActivity(between_page);
				this.finish();
			}	
		}
	}
	public void updateScore(){
		int score = Settings.getScore() + timeCounter;
        Settings.setScore(score);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    menu.add("Exit"); 
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{  
	    if (item.getTitle() == "Exit")
			t.cancel();
	    	Settings.resetAll();
	    	this.finish();
	    return super.onOptionsItemSelected(item);    
	}
	@Override
	public void onBackPressed() {}
	
    @Override 
    public void onConfigurationChanged(Configuration newConfig)
    {
       super.onConfigurationChanged(newConfig);
    }
}


	
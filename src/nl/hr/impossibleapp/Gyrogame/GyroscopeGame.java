package nl.hr.impossibleapp.Gyrogame;

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

import nl.hr.impossibleapp.Activity_Between;
import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.Sound;
import nl.hr.impossibleapp.Settings;
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
	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
    private boolean addedOnce = false;
	// Collision detection values
	private boolean up = true;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	private int countdown = 1;
	
	// Check if activity is running
	static boolean active = false;
	//Ball variables
	BallScript myBall = null;
    Handler RedrawHandler = new Handler(); 
    Timer ballTimer = null;
    TimerTask ballTask = null;
    int screenWidth, screenHeight;
    android.graphics.PointF ballPos, ballSpeed, squarePos;
    
    // Square variables
    SquareScript mySquareU = null;
    SquareScript mySquareD = null;
    SquareScript mySquareL = null;
    SquareScript mySquareR = null;
    long millisUntilFinished = 12000;
    
	//Textview for timer
    private TextView timerField;
	private Timer t = new Timer();
	private int TimeCounter = 12;
	
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
    	// background thread
        ballTimer.cancel(); 
        ballTimer = null;
        ballTask = null;
        super.onPause();
    	this.finish();
    }
    // supressed getwidth/getheight
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_gyrogame);
	    
	    // view where the ball is placed
	    final FrameLayout mainView = 
	    (android.widget.FrameLayout)findViewById(R.id.gyrogame_view);
	    
	    
	    // Loading Font Face
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		
		// Textfield from xml
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
	    
		// get dimension values of the screen
		Display display = getWindowManager().getDefaultDisplay();  
		screenWidth = display.getWidth(); 
		screenHeight = display.getHeight();
		ballPos = new android.graphics.PointF();
		ballSpeed = new android.graphics.PointF();
		        
		// Ball position and starting speed
		ballPos.x = screenWidth/2; 
		ballPos.y = screenHeight/2; 
		ballSpeed.x = 0;
		ballSpeed.y = 0; 
		
		squarePos = new android.graphics.PointF();
		squarePos.x = screenWidth/4;
		squarePos.y = screenHeight/2;
		
		// Ball to be placed on screen
		myBall = new BallScript(this, ballPos.x, ballPos.y, 10);
		mySquareU = new SquareScript(this, 0,0,screenWidth,screenHeight,true,false,false,false);
		mySquareD = new SquareScript(this, 0,0,screenWidth,screenHeight,false,true,false,false);
		mySquareL = new SquareScript(this, 0,0,screenWidth,screenHeight,false,false,true,false);
		mySquareR = new SquareScript(this, 0,0,screenWidth,screenHeight,false,false,false,true);
		
		mySquareU.setVisibility(View.VISIBLE);
		mySquareD.setVisibility(View.INVISIBLE);
		mySquareL.setVisibility(View.INVISIBLE);	
		mySquareR.setVisibility(View.INVISIBLE);
		
		// Add the ball and square to the mainView
		mainView.addView(mySquareU);
		mainView.addView(mySquareD);
		mainView.addView(mySquareL);
		mainView.addView(mySquareR);
		mainView.addView(myBall);
		// onDraw in the ball script
		myBall.invalidate(); 
		//Eventlistener for sensorevents
		((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
		     new SensorEventListener() {    
		        @Override  
		        public void onSensorChanged(SensorEvent event) {  
		           // ball speed based on pitch and roll
		           ballSpeed.y = event.values[0];
		           ballSpeed.x = event.values[1];
		        }
		        @Override  
		        public void onAccuracyChanged(Sensor sensor, int accuracy) {} //this makes sure accuracychanged has no effect on the ball
		    },
		    // Sensor to check for is the Accelerometer
		    ((SensorManager)getSystemService(Context.SENSOR_SERVICE))
		    .getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),   
		     SensorManager.SENSOR_DELAY_NORMAL);
	} //OnCreate
	
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
	                    timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(TimeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    TimeCounter--;
	                    countdown --;
	    	            if (countdown== 0)
	    	            {
	    	            	if (up)
	    	            	{
	    	            		up = false;
	    	            		down = true;
	    	            		mySquareU.setVisibility(View.INVISIBLE);
	    	            		mySquareD.setVisibility(View.VISIBLE);
	    	            	}
	    	            	else if (down)
	    	            	{
	    	            		down = false;
	    	            		left = true;
	    	            		mySquareD.setVisibility(View.INVISIBLE);
	    	            		mySquareL.setVisibility(View.VISIBLE);
	    	            	}
	    	            	else if (left)
	    	            	{
	    	            		left = false;
	    	            		right = true;
	    	            		mySquareL.setVisibility(View.INVISIBLE);
	    	            		mySquareR.setVisibility(View.VISIBLE);
	    	            	}
	    	            	else if (right)
	    	            	{
	    	            		right = false;
	    	            		up = true;
	    	            		mySquareR.setVisibility(View.INVISIBLE);
	    	            		mySquareU.setVisibility(View.VISIBLE);
	    	            	}
	    	            	countdown = 1;
	    	            }
	                    if(TimeCounter == 4){
	                    	Sound.countDown(getBaseContext());
	                    }
	                    if (TimeCounter < 0)
	                    {
	                    	Sound.gameOver(getBaseContext());
	            	    	System.out.println("timelowerthen1");
	                    	TimeCounter = 12;
	                    	Settings.setLives(Settings.getLives() - 1);
	                    	betweenScreen();
	                    	t.cancel();
	                    }
	                }
	            });
	        }
	    }, 0, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
	 
	    //create timer to move ball to new position
	    ballTimer = new Timer(); 
	    ballTask = new TimerTask() {
	    public void run() {
	    	
	    //  a log for the ball
	    android.util.Log.d("TiltBall","Timer Hit - " + ballPos.x + ":" + ballPos.y);
	    
	    //move ball based on current speed
	    ballPos.x += ballSpeed.x;
	    ballPos.y += ballSpeed.y;
	    
	    //if ball moves out of the screen, make it stop
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
	    
	    //update ball location on screen
	    myBall.x = ballPos.x;
	    myBall.y = ballPos.y;
	    //redraw ball.
	    RedrawHandler.post(new Runnable() {
	        public void run() {    
	        myBall.invalidate();
	        }});
	    }}; // TimerTask
	    
	    ballTimer.schedule(ballTask,10,10); //start timer
	    super.onResume();
	} // onResume
	public void betweenScreen()
	{
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent between_page = new Intent(this, Activity_Between.class);
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
		int score = Settings.getScore() + TimeCounter;
        Settings.setScore(score);
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
	    	this.finish(); //will call onPause
	    return super.onOptionsItemSelected(item);    
	}
	@Override
	public void onBackPressed() {}
	
    //listener for config change, so it stays in landscape mode
    @Override 
    public void onConfigurationChanged(Configuration newConfig)
    {
       super.onConfigurationChanged(newConfig);
    }
}


	
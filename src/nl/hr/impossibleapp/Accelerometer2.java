package nl.hr.impossibleapp;


import java.util.Timer;
import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Accelerometer2 extends Activity implements SensorEventListener {
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private boolean active = false;
	
	private float mLastX, mLastY, mLastZ;
	private int countShakes = 0;
	private boolean mInitialized;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;

    private TextView timerBox;

    private Timer t = new Timer();
    private int timeCounter = 15;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.layout_accelerometer2);
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        ImageView heartsField = (ImageView) findViewById(R.id.hearts);
        int difficulty = Settings.getDifficulty();
        if(difficulty == 3){
        	timeCounter = 10;
        }else if(difficulty == 2){
        	timeCounter = 12;
        }
        
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
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
	                	timerBox.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    if(active){
	                    	timeCounter--;
	                    	
	                    	if(timeCounter  == 4){
		                    	Sound.countDown(getBaseContext());
		                    }
		                    if (timeCounter < 0)
		                    {
		                    	Sound.gameOver(getBaseContext());
		                    	Settings.setLives(Settings.getLives() - 1);
		                    	System.out.println("[Accelerometer2] Minus life: out of time, " + Settings.getLives());
		                    	betweenScreen();
		                    	t.cancel();
		                    }
	                    }
	                 }
	            });
	        }
	    }, 0, 1000);
    }
    public void betweenScreen(){
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent between_page = new Intent(this, ActivityBetween.class);
		between_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		Settings.addScore(timeCounter);
		if(between_page != null){
			if (active){
				startActivity(between_page);
			}	
		}
		finish();
	}

    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy){}

	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		TextView tvShakes = (TextView)findViewById(R.id.countShakes);
		tvShakes.setTypeface(tf);

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		
		if (!mInitialized){
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		}else{
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE) deltaX = (float)0.0;
			if (deltaY < NOISE) deltaY = (float)0.0;
			if (deltaZ < NOISE) deltaZ = (float)0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			tvShakes.setText(getResources().getString(R.string.shakes) + ": " + Integer.toString(countShakes));
			
			if(deltaX > 0 || deltaX <0 || deltaY > 0 || deltaY < 0){
				countShakes++;
			}
			
			if(countShakes > 50){
				Sound.wonMinigame(getBaseContext());
				betweenScreen();
			}
		}
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
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public void onBackPressed(){}
}
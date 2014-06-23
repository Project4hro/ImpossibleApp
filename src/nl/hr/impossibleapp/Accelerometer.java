package nl.hr.impossibleapp;


import java.util.Random;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Accelerometer extends Activity implements SensorEventListener{
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private boolean active = false;
	
	private float mLastX, mLastY, mLastZ;
	private int countShakes = 0;
	private int timeCounter = 10;
	private boolean mInitialized;
	private boolean allowToShake = false;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
    
    private TextView tvTimerInt;
    private Timer t = new Timer();
    
    private TextView readyGo;
    private boolean endedGame = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	setContentView(R.layout.layout_accelerometer);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        ImageView heartsField = (ImageView) findViewById(R.id.hearts);
        int lives = Settings.getLives();
        if(lives == 2){
        	heartsField.setImageResource(R.drawable.heart2);
        }else if(lives == 1){
        	heartsField.setImageResource(R.drawable.heart1);
        }else{
        	heartsField.setImageResource(R.drawable.heart3);
        }
        
        readyGo = (TextView) findViewById(R.id.readyGo);
		readyGo.setTypeface(tf);
		
		tvTimerInt = (TextView)findViewById(R.id.timerBox);
		tvTimerInt.setTypeface(tf);
		TextView scoreText = (TextView) findViewById(R.id.scoreBox);
		scoreText.setTypeface(tf);
	    scoreText.setText("Score: " + Settings.getScore());
	    
	    Sound.Ready(getBaseContext());
	    readyGo.setText("Ready?");
	    
	    startTimer();
    }
    
    private void startTimer(){
    	final int goInt = new Random().nextInt(5) + 3;
    	
    	t.scheduleAtFixedRate(new TimerTask(){
	        @Override
	        public void run() {
	            
	       	runOnUiThread(new Runnable(){
	                public void run(){
	                    tvTimerInt.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s");
	                    if(active){
	                    	timeCounter--;
	                    	
	                    	if(goInt == timeCounter){
		                    	allowToShake = true;
		                    	Sound.Go(getBaseContext());
		                    	readyGo.setText("GO!");
		                    }
		                    
		                    if(timeCounter  == 2){
		                    	Sound.shortCountDown(getBaseContext());
		                    	
		                    }
		                    if (timeCounter < 0){
		                    	Sound.gameOver(getBaseContext());
		                    	Settings.setLives(Settings.getLives() - 1);
		                    	System.out.println("[Accelerometer] Minus life: out of time, " + Settings.getLives());
		                    	betweenScreen();
		                    	t.cancel();
		                    }
	                    }
	                 }
	            });
	        }
	    }, 0, 1000);
    }
    protected void onStart(){
    	super.onStart();
        active = true;
    }
    
    public void onStop(){
        super.onStop();
        active = false;
    }
    
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    private void betweenScreen(){
    	Log.i("accelerometer", "klaar");
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent between_page = new Intent(this, ActivityBetween.class);
        Settings.addScore(timeCounter);
		if(between_page != null){
			if (active && endedGame){
				startActivity(between_page);
				this.finish();
				endedGame = false;
			}	
		}
	}

    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy){}

	@Override
	public void onSensorChanged(SensorEvent event){
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

			
			if(deltaX > 0 || deltaX <0 || deltaY > 0 || deltaY < 0){
				countShakes++;
				System.out.println(countShakes);
			}
			if(allowToShake){	
				if(countShakes > 20){
					Sound.wonMinigame(getBaseContext());
                	endedGame = true;
					betweenScreen();
				}
			}else{
				if(countShakes > 10){
					Sound.gameOver(getBaseContext());
                	Settings.setLives(Settings.getLives() - 1);
                	System.out.println("[Accelerometer] Game over, " + Settings.getLives());
                	endedGame = true;
                	betweenScreen();
                	t.cancel();
				}
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
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public void onBackPressed(){}
}
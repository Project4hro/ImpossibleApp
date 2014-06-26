package nl.hr.impossibleapp;

import java.util.Random;
import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.activities.ActivityTemplate;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Accelerometer extends ActivityTemplate implements SensorEventListener{
    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
	
	private float mLastX, mLastY, mLastZ;
	private int countShakes = 0;
	private int timeCounter = 10;
	private boolean mInitialized;
	private boolean allowToShake = false;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float noise = (float) 2.0;
    
    private TextView tvTimerInt;
    private TextView scoreView;
	private ImageView heartsView;
	
    private TextView readyGo;
    private boolean endedGame = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	setContentView(R.layout.layout_accelerometer);
	    tf = Typeface.createFromAsset(getAssets(), FONTPATH);
	    
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        heartsView = (ImageView) findViewById(R.id.hearts);
        setHeartsView(heartsView);
        
        readyGo = (TextView) findViewById(R.id.readyGo);
		readyGo.setTypeface(tf);
		
		tvTimerInt = (TextView)findViewById(R.id.timerBox);
		tvTimerInt.setTypeface(tf);
		scoreView = (TextView) findViewById(R.id.scoreBox);
		setScoreView(scoreView, getAssets());
	    
	    Sound.ready(getBaseContext());
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
		                    	Sound.go(getBaseContext());
		                    	readyGo.setText("GO!");
		                    }
		                    
		                    if(timeCounter  == 2){
		                    	Sound.shortCountDown(getBaseContext());
		                    	
		                    }
		                    if (timeCounter < 0){
		                    	Sound.gameOver(getBaseContext());
		                    	Settings.setLives(Settings.getLives() - 1);
		                    	endedGame = true;
		                    	betweenScreen();
		                    	t.cancel();
		                    }
	                    }
	                 }
	            });
	        }
	    }, 0, 1000);
    }
    
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        active = true;
    }
    
    private void betweenScreen(){
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent betweenPage = new Intent(this, ActivityBetween.class);
        Settings.addScore(timeCounter);
		if(betweenPage != null && active && endedGame){
			startActivity(betweenPage);
			this.finish();
			endedGame = false;
		}
	}

    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy){
		// unused
	}

	@Override
	public void onSensorChanged(SensorEvent event){
		// check if shaked
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
			if (deltaX < noise){
				deltaX = (float)0.0;
			}
			if (deltaY < noise){
				deltaY = (float)0.0;
			}
			if (deltaZ < noise){
				deltaZ = (float)0.0;
			}
			mLastX = x;
			mLastY = y;
			mLastZ = z;

			
			if(deltaX > 0 || deltaX <0 || deltaY > 0 || deltaY < 0){
				countShakes++;
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
                	endedGame = true;
                	betweenScreen();
                	t.cancel();
				}
			}
		}
	}
}
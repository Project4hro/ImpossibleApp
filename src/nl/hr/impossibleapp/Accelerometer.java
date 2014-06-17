package nl.hr.impossibleapp;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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

public class Accelerometer extends Activity implements SensorEventListener 
{
	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	static boolean active = false;
	
	private float mLastX, mLastY, mLastZ;
	private int countShakes = 0;
	private int TimeCounter = 10;
	private boolean mInitialized;
	private boolean allowToShake = false;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
    
    private TextView tvTimerInt;
  //Textview for timer
    private Timer t;
    
    
    
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
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
        
        final TextView readyGo = (TextView) findViewById(R.id.readyGo);

		readyGo.setTypeface(tf);
		
		tvTimerInt = (TextView)findViewById(R.id.timerBox);
		tvTimerInt.setTypeface(tf);
		TextView scoreText = (TextView) findViewById(R.id.scoreBox);
		scoreText.setTypeface(tf);
	    scoreText.setText("Score: " + Settings.getScore());
	    
	    Random randomGenerator = new Random();
	    final int goInt = randomGenerator.nextInt(5) + 3;
	    
	    //Text and Sound for "Ready"
	    Sound.Ready(getBaseContext());
	    readyGo.setText("Ready?");
	    
		t = new Timer();
	    t.scheduleAtFixedRate(new TimerTask() 
	    {
	        @Override
	        public void run() {
	            
	       	runOnUiThread(new Runnable() 
	            {
	                public void run() 
	                {
	                    tvTimerInt.setText(String.valueOf(TimeCounter)); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    TimeCounter--;
	                    
	                    
	                    
	                    if(goInt == TimeCounter)
	                    {
	                    	allowToShake = true;
	                    	Sound.Go(getBaseContext());
	                    	readyGo.setText("GO!");
	                    }
	                    
	                    if(TimeCounter  == 2){
	                    	Sound.shortCountDown(getBaseContext());
	                    	
	                    }
	                    if (TimeCounter < 0)
	                    {
	                    	Sound.gameOver(getBaseContext());
	                    	Settings.setLives(Settings.getLives() - 1);
	                    	System.out.println("[Accelerometer] Minus life: out of time, " + Settings.getLives());
	                    	TimeCounter = 10;
	                    	betweenScreen();
	                    	t.cancel();
	                    }
	                 }
	            });
	        }
	    }, 0, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
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
    
    public void betweenScreen()
	{
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		// check currentGame
		Intent between_page = new Intent(this, Activity_Between.class);
		int score = Settings.getScore() + TimeCounter;
        Settings.setScore(score);
		if(between_page != null)
		{
			if (active)
			{
				startActivity(between_page);
			}	
		}
		this.finish();
	}

    protected void onPause() 
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) 
	{
		// can be safely ignored for this demo
	}

	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		if (!mInitialized) 
		{
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		} 
		else 
		{
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE) deltaX = (float)0.0;
			if (deltaY < NOISE) deltaY = (float)0.0;
			if (deltaZ < NOISE) deltaZ = (float)0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;

			
			if(deltaX > 0 || deltaX <0 || deltaY > 0 || deltaY < 0)
			{
				countShakes++;
				System.out.println(countShakes);
			}
			if(allowToShake)
			{	
				if(countShakes > 20)
				{
					Sound.wonMinigame(getBaseContext());
					betweenScreen();
				}
			}
			else
			{
				if(countShakes > 10)
				{
					Sound.gameOver(getBaseContext());
                	Settings.setLives(Settings.getLives() - 1);
                	System.out.println("[Accelerometer] Game over, " + Settings.getLives());
                	TimeCounter = 10;
                	betweenScreen();
                	t.cancel();
				}
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
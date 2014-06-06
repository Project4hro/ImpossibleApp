package nl.hr.impossibleapp;


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
import android.widget.LinearLayout;
import android.widget.TextView;

public class Accelerometer2 extends Activity implements SensorEventListener 
{
	static boolean active = false;
	
	private float mLastX, mLastY, mLastZ;
	private int countShakes = 0;
	private boolean mInitialized;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
    private Typeface tf;
    private TextView tvTimerInt;
  //Textview for timer
    private Timer t;
    private int TimeCounter = 10;
    
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.layout_accelerometer);
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        ImageView heartsField = (ImageView) findViewById(R.id.hearts);
        int lives = settings.getLives();
        if(lives == 2){
        	heartsField.setImageResource(R.drawable.heart2);
        }else if(lives == 1){
        	heartsField.setImageResource(R.drawable.heart1);
        }else{
        	heartsField.setImageResource(R.drawable.heart3);
        }
        // Font path
	    String fontPath = "fonts/mvboli.ttf";
	    // Loading Font Face
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		tvTimerInt = (TextView)findViewById(R.id.timerInt);
		
		TextView scoreText = (TextView) findViewById(R.id.scoreBox);
	    scoreText.setText("Score: " + settings.getScore());
	    
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
	                    
	                    if(TimeCounter  == 4){
	                    	Sound.countDown(getBaseContext());
	                    }
	                    if (TimeCounter < 1)
	                    {
	                    	Sound.gameOver(getBaseContext());
	                    	settings.setLives(settings.getLives() - 1);
	                    	System.out.println("[Accelerometer2] Minus life: out of time, " + settings.getLives());
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
		// check currentGame
		//Intent mIntent = getIntent();
		//int currentGame = mIntent.getIntExtra("intCurrentGame", 0);
		Intent between_page = new Intent(this, Activity_Between.class);
		//between_page.putExtra("intCurrentGame", currentGame);
		//between_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		int score = settings.getScore() + TimeCounter;
        settings.setScore(score);
		if(between_page != null)
		{
			if (active)
			{
				startActivity(between_page);
			}	
		}
		finish();
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
		TextView tvShakes = (TextView)findViewById(R.id.countShakesInt);
		tvShakes.setTypeface(tf);
		//ImageView ivCocktailShaker = (ImageView)findViewById(R.id.cocktailShaker);
		//LinearLayout.LayoutParams lpCocktailShaker = (LinearLayout.LayoutParams) ivCocktailShaker.getLayoutParams();
		//ImageView ivHandTop = (ImageView)findViewById(R.id.handTop);
		//LinearLayout.LayoutParams lpHandTop = (LinearLayout.LayoutParams) ivHandTop.getLayoutParams();
		//ImageView ivHandBottem = (ImageView)findViewById(R.id.handBottem);
		//LinearLayout.LayoutParams lpHandBottem = (LinearLayout.LayoutParams) ivHandBottem.getLayoutParams();
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		
		/*lpCocktailShaker.setMargins(350, 20, 0, 0);
		lpHandTop.setMargins(400, 20, 0, 0);
		lpHandBottem.setMargins(350, 20, 0, 0);*/
		
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
			tvShakes.setText(Integer.toString(countShakes));
			
			if(deltaX > 0 || deltaX <0 || deltaY > 0 || deltaY < 0)
			{
				countShakes++;
				System.out.println(countShakes);
			}
			
			if(countShakes > 50)
			{
				betweenScreen();
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
	    	this.finish(); //will call onPause
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
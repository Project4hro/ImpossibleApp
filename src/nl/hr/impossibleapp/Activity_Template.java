package nl.hr.impossibleapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Activity_Template extends Activity 
{
	static boolean active = false;
	
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
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_template);

	    // Font path
	    String fontPath = "fonts/mvboli.ttf";
	    // Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		// Textfield from xml
		final TextView timerField = (TextView) findViewById(R.id.timerBox);
		timerField.setTypeface(tf);
				
	    // Countdown Timer (total*1000,subtraction*1000)
	    new CountDownTimer(12000, 1000) 
	    {
		    // What is done every tick
	        public void onTick(long millisUntilFinished)
	        {
	            timerField.setText(""+(millisUntilFinished / 1000 - 1));
	            if ((millisUntilFinished / 1000)< 2)
	            {
	            	betweenScreen();
	            }
	        }
	        // When timer reaches zero
	        public void onFinish() 
	        {
	        }
	     }.start();
	        
	}
	public void betweenScreen()
	{
		Intent between_page = new Intent(this, Activity_Between.class);
		if(between_page != null){
			if (active)
			{
				startActivity(between_page);
			}
			this.finish();	
		}
	}
}


	
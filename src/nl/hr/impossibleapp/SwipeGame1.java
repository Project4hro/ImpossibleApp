package nl.hr.impossibleapp;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SwipeGame1 extends Activity
{
	private int randInt;
	float x1, x2;
	float y1, y2;

	int x = 0;
	int arraylistKey = 0;
	int amountAssignments = 4;
	int lives;
	TextView assignment;
	TextView message;
	ImageView heartsField;
	
	MediaPlayer mp;
	
	String swipeDirection;
	ArrayList<Integer> assignments = new ArrayList<Integer>();
	Timer mTmr = null;
    TimerTask mTsk = null;
    
    TextView timerField;
    Timer t;
    int TimeCounter = 20;
    
	// Check if activity is running
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
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_swipegame1);
	    if(settings.getDifficulty() == 1){
	    	amountAssignments = 4;
	    }else if(settings.getDifficulty() == 2){
	    	amountAssignments = 6;
	    }else if(settings.getDifficulty() == 3){
	    	amountAssignments = 8;
	    }
	    assignments.clear();
	    assignment = (TextView) findViewById(R.id.messageAssignment);
		message = (TextView) findViewById(R.id.errMessage);
		timerField = (TextView) findViewById(R.id.timerBox);
		heartsField = (ImageView) findViewById(R.id.hearts);		
	    randInt = new Random().nextInt(4) + 1;
	    lives = settings.getLives();
	    if(lives == 2){
	    	heartsField.setImageResource(R.drawable.heart2);
	    }else if(lives == 1){
	    	heartsField.setImageResource(R.drawable.heart1);
	    }
	    changeAssignment();
	    
	    t = new Timer();
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
	    				
	    				if (TimeCounter < 1)
	    				{
	    					TimeCounter = 20;
	    					betweenScreen();
	    					t.cancel();
                     	}
                 	}
	    		});

	    	}
	     }, 0, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
	} //OnCreate
	public void playSound(String sound){
		if(settings.isSoundEnabled() == true){
			if(mp != null){
				mp.release();
			}
			mp = null;
			if(sound == "correct"){
				mp = MediaPlayer.create(this, R.raw.ding);
			}else if(sound == "wrong"){
				mp = MediaPlayer.create(this, R.raw.wrong);
			}else if(sound == "gameover"){
				mp = MediaPlayer.create(this, R.raw.fail);
			}
			mp.start();
		}
	}
	
	public void changeAssignment()
	{
		if(x < (amountAssignments - 1))
		{
			if(randInt == 1){
		    	swipeDirection = "Swipe "+getResources().getString(R.string.left);
		    }else if(randInt == 2){
		    	swipeDirection = "Swipe "+getResources().getString(R.string.right);
		    }else if(randInt == 3){
		    	swipeDirection = "Swipe "+getResources().getString(R.string.up);
		    }else if(randInt == 4){
		    	swipeDirection = "Swipe "+getResources().getString(R.string.down);
		    }else{
		    	swipeDirection = "Swipe all sides";
		    }
			assignments.add(randInt);
		}else{
			swipeDirection = getResources().getString(R.string.repeatSteps);
		}
		assignment.setText(swipeDirection);
	}
	
	public void restart()
	{
		t.cancel();
	}
	
	public void won(){
		if(arraylistKey == assignments.size())
		{
			assignment.setText("Gewonnen!");
			t.cancel();
		}
	}
	
	public boolean onTouchEvent(MotionEvent touchevent)
	{
		String wrong = getResources().getString(R.string.wrongAnswer);
		String correct = getResources().getString(R.string.rightAnswer);
		switch(touchevent.getAction()){
			case MotionEvent.ACTION_DOWN:
			{
				x1 = touchevent.getX();
				y1 = touchevent.getY();
				break;
			}
			case MotionEvent.ACTION_UP:
			{
				if(arraylistKey == assignments.size()){
					t.cancel();
				    assignments.clear();
					this.finish();
					break;
				}
				if(message.getText().toString() == "Game over!")
				{
					t.cancel();
				    assignments.clear();
					this.finish();
					break;
				}
				x2 = touchevent.getX();
				y2 = touchevent.getY();
				
				if(x == amountAssignments)
				{
					randInt = assignments.get(arraylistKey);
				}
				
				if(randInt == 1)
				{
					if((x1 - x2) > 180){
						System.out.println("links " + (x1 - x2));
						message.setText(correct);
						playSound("correct");
						if(x < amountAssignments){
							randInt = new Random().nextInt(4) + 1;
							changeAssignment();
							x++;
						}else{
							message.setText(correct + " herhaal");
							arraylistKey++;
							won();
						}
						break;
					}else{
						if(x == amountAssignments){
							message.setText("Game over!");
							playSound("gameover");
							restart();
						}else{
							message.setText(wrong);
							playSound("wrong");
						}
						break;
					}
				}
				if(randInt == 2){
					if((x1 - x2) < -180){
						System.out.println("rechts " + (x1 - x2));
						message.setText(correct);
						playSound("correct");
						if(x < amountAssignments)
						{
							randInt = new Random().nextInt(4) + 1;
							changeAssignment();
							x++;
						}
						else
						{
							message.setText(correct + " herhaal");
							arraylistKey++;
							won();
						}
						break;
					}
					else
					{
						if(x == amountAssignments)
						{
							message.setText("Game over!");
							playSound("gameover");
							restart();
						}else{
							message.setText(wrong);
							playSound("wrong");
						}
						break;
					}
				}
			
				if(randInt == 3)
				{
					if((y1 - y2) > 150)
					{
						System.out.println("boven " + (y1 - y2));
						message.setText(correct);
						playSound("correct");
						if(x < amountAssignments)
						{
							randInt = new Random().nextInt(4) + 1;
							changeAssignment();
							x++;
						}
						else
						{
							message.setText(correct + " herhaal");
							arraylistKey++;
							won();
						}
						break;
					}
					else
					{
						if(x == amountAssignments)
						{
							message.setText("Game over!");
							playSound("gameover");
							restart();
						}
						else
						{
							message.setText(wrong);
							playSound("wrong");
						}
						break;
					}			
				}
				if(randInt == 4)
				{
					if((y1 - y2) < -150)
					{
						System.out.println("onder " + (y1 - y2));
						message.setText(correct);
						playSound("correct");
						if(x < amountAssignments)
						{
							randInt = new Random().nextInt(4) + 1;
							changeAssignment();
							x++;
						}
						else
						{
							message.setText(correct + " herhaal");
							arraylistKey++;
							won();
						}
						break;
					}
					else
					{
						if(x == amountAssignments)
						{
							message.setText("Game over!");
							playSound("gameover");
							restart();
						}
						else
						{
							message.setText(wrong);
							playSound("wrong");
						}
						break;
					}
				}
				break;
			}
		}
		return false;
	} //onTouchEvent
	
	public void betweenScreen()
	{
		t.cancel();
		// check currentGame
		Intent mIntent = getIntent();
		int currentGame = mIntent.getIntExtra("intCurrentGame", 0);
		Intent between_page = new Intent(this, Activity_Between.class);
		between_page.putExtra("intCurrentGame", currentGame);
		//between_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(between_page != null)
		{
			if (active)
			{
				startActivity(between_page);
			}	
		}
		finish();
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

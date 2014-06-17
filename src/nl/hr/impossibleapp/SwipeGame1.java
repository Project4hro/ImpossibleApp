package nl.hr.impossibleapp;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.activities.ActivityMenu;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SwipeGame1 extends Activity{
	
	private static final String TAG = SwipeGame1.class.toString(); 

    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private int randInt;
	private float x1, x2;
	private float y1, y2;

	private int x = 0;
	private int arraylistKey = 0;
	private int amountAssignments = 4;
	
	private TextView assignment;
	private TextView assignment1;
	private TextView assignment2;
	private TextView message;
	private TextView message1;
	private TextView message2;
	
	private String swipeDirection;
	private ArrayList<Integer> assignments = new ArrayList<Integer>();
    
    private TextView timerField;
    private static Timer t = new Timer();
    private int timeCounter = 20;
    
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
	    setContentView(R.layout.layout_swipegame1);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    
	    if(Settings.getDifficulty() == 1){
	    	amountAssignments = 4;
	    }else if(Settings.getDifficulty() == 2){
	    	amountAssignments = 6;
	    }else if(Settings.getDifficulty() == 3){
	    	amountAssignments = 8;
	    }
	    assignments.clear();
	    assignment1 = (TextView) findViewById(R.id.messageAssignment1);
	    assignment2 = (TextView) findViewById(R.id.messageAssignment2);
	    assignment = assignment1;
		message1 = (TextView) findViewById(R.id.errMessage1);
		message2 = (TextView) findViewById(R.id.errMessage2);
		message = message1;
		timerField = (TextView) findViewById(R.id.timerBox);
		assignment1.setTypeface(tf);
		assignment2.setTypeface(tf);
		message1.setTypeface(tf);
		message2.setTypeface(tf);
		timerField.setTypeface(tf);
		// set hearts view
		ImageView heartsField = (ImageView) findViewById(R.id.hearts);		
	    randInt = new Random().nextInt(4) + 1;
	    int lives = Settings.getLives();
	    if(lives == 2){
	    	heartsField.setImageResource(R.drawable.heart2);
	    }else if(lives == 1){
	    	heartsField.setImageResource(R.drawable.heart1);
	    }else{
	    	heartsField.setImageResource(R.drawable.heart3);
	    }
	    
	    // set scoreview
	    TextView scoreText = (TextView) findViewById(R.id.scoreBox);
	    scoreText.setTypeface(tf);
	    scoreText.setText("Score: " + Settings.getScore());
	    changeAssignment();
	    startTimer();
	} //OnCreate
	
	// change swipe direction based on random int
	private void changeAssignment(){
		if(assignment1.getText().toString() == ""){
			assignment = assignment1;
			assignment2.setText("");
		}else{
			assignment = assignment2;
			assignment1.setText("");
		}
		if(x < (amountAssignments - 1)){
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
	
	private void won(boolean won){
		if(won){
			if(arraylistKey == assignments.size()){
				t.cancel();
				if(assignment1.getText().toString() == ""){
					assignment = assignment1;
					assignment2.setText("");
				}else{
					assignment = assignment2;
					assignment1.setText("");
				}
				Sound.wonMinigame(getBaseContext());
				assignment.setText("Gewonnen!");
				int score = Settings.getScore() + timeCounter;
		        Settings.setScore(score);
				betweenScreen();
			}
		}else{
			t.cancel();
			assignments.clear();
		    Settings.setLives(Settings.getLives() - 1);
        	System.out.println("Minus life: lost game, " + Settings.getLives());
			betweenScreen();
		}
	}
	private void startTimer(){
	    t.scheduleAtFixedRate(new TimerTask(){
	    	@Override
	    	public void run() {
	    		runOnUiThread(new Runnable(){
	    			public void run(){
	    				timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	    				timeCounter--;
	    				
	    				if(timeCounter  == 4){
	                    	Sound.countDown(getBaseContext());
	                    }
	    				
	    				if (timeCounter < 0){
	    					Sound.gameOver(getBaseContext());
	    					timeCounter = 20;
	                    	Settings.setLives(Settings.getLives() - 1);
	                    	System.out.println("[SwipeGame1] Minus life: out of time, " + Settings.getLives());
	                    	Log.d(TAG, "Time " + timeCounter);
	    					betweenScreen();
	    					t.cancel();
                     	}
                 	}
	    		});

	    	}
	     }, 0, 1000);
	}
	
	public boolean onTouchEvent(MotionEvent touchevent){
		String wrong = getResources().getString(R.string.wrongAnswer);
		String correct = getResources().getString(R.string.rightAnswer);
		switch(touchevent.getAction()){
			case MotionEvent.ACTION_DOWN:
				
				x1 = touchevent.getX();
				y1 = touchevent.getY();
				break;
			
			case MotionEvent.ACTION_UP:
				
				x2 = touchevent.getX();
				y2 = touchevent.getY();
				
				//check if amountAssignments is met, if yes repeat steps
				if(x == amountAssignments){
					randInt = assignments.get(arraylistKey);
				}

				if(message1.getText().toString() == ""){
					message = message1;
					message2.setText("");
				}else{
					message = message2;
					message1.setText("");
				}
				// check if step is corret
				if(randInt == 1){
					if((x1 - x2) > 180){
						System.out.println("links " + (x1 - x2));
						message.setText(correct);
						Sound.correct(getBaseContext());
						if(x < amountAssignments){
							randInt = new Random().nextInt(4) + 1;
							changeAssignment();
							x++;
						}else{
							message.setText(correct);
							arraylistKey++;
							won(true);
						}
						break;
					}else{
						if(x == amountAssignments){
							message.setText("Game over!");
							Sound.gameOver(getBaseContext());
							won(false);
						}else{
							message.setText(wrong);
							Sound.wrong(getBaseContext());
						}
						break;
					}
				}
				if(randInt == 2){
					if((x1 - x2) < -180){
						System.out.println("rechts " + (x1 - x2));
						message.setText(correct);
						Sound.correct(getBaseContext());
						if(x < amountAssignments){
							randInt = new Random().nextInt(4) + 1;
							changeAssignment();
							x++;
						}else{
							message.setText(correct);
							arraylistKey++;
							won(true);
						}
						break;
					}else{
						if(x == amountAssignments){
							message.setText("Game over!");
							Sound.gameOver(getBaseContext());
							won(false);
						}else{
							message.setText(wrong);
							Sound.wrong(getBaseContext());
						}
						break;
					}
				}
			
				if(randInt == 3){
					if((y1 - y2) > 150){
						System.out.println("boven " + (y1 - y2));
						message.setText(correct);
						Sound.correct(getBaseContext());
						if(x < amountAssignments){
							randInt = new Random().nextInt(4) + 1;
							changeAssignment();
							x++;
						}else{
							message.setText(correct);
							arraylistKey++;
							won(true);
						}
						break;
					}else{
						if(x == amountAssignments)
						{
							message.setText("Game over!");
							Sound.gameOver(getBaseContext());
							won(false);
						}else{
							message.setText(wrong);
							Sound.wrong(getBaseContext());
						}
						break;
					}			
				}
				if(randInt == 4){
					if((y1 - y2) < -150){
						System.out.println("onder " + (y1 - y2));
						message.setText(correct);
						Sound.correct(getBaseContext());
						if(x < amountAssignments)
						{
							randInt = new Random().nextInt(4) + 1;
							changeAssignment();
							x++;
						}else{
							message.setText(correct);
							arraylistKey++;
							won(true);
						}
						break;
					}else{
						if(x == amountAssignments){
							message.setText("Game over!");
							Sound.gameOver(getBaseContext());
							won(false);
						}else{
							message.setText(wrong);
							Sound.wrong(getBaseContext());
						}
						break;
					}
				}
				break;
			}
		
		return false;
	} //onTouchEvent
	
	private void betweenScreen(){
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent between_page = new Intent(this, ActivityBetween.class);
		
		between_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(between_page != null){
			if (active){
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
			t.cancel();Intent menu_page = new Intent(this, ActivityMenu.class);
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

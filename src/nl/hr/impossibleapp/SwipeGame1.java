package nl.hr.impossibleapp;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.activities.ActivityTemplate;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SwipeGame1 extends ActivityTemplate{
	
	private static final String TAG = SwipeGame1.class.toString(); 

    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private int randInt;
	private float x1, x2;
	private float y1, y2;

	private TextView scoreView;
	private ImageView heartsView;
	
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
	private String correct;
	private String wrong;
	private ArrayList<Integer> assignments = new ArrayList<Integer>();
    
    private TextView timerField;
    private int timeCounter = 20;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_swipegame1);
	    tf = Typeface.createFromAsset(getAssets(), FONTPATH);
	    
	    if(Settings.getDifficulty() == 1){
	    	amountAssignments = 4;
	    }else if(Settings.getDifficulty() == 2){
	    	amountAssignments = 6;
	    }else if(Settings.getDifficulty() == 3){
	    	amountAssignments = 8;
	    }
	    
	    wrong = getResources().getString(R.string.wrongAnswer);
		correct = getResources().getString(R.string.rightAnswer);
		
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

		heartsView = (ImageView) findViewById(R.id.hearts);	
		setHeartsView(heartsView);
	    
		scoreView = (TextView) findViewById(R.id.scoreBox);
	    setScoreView(scoreView, getAssets());
	    
		randInt = new Random().nextInt(4) + 1;
	    
	    changeAssignment();
	    startTimer();
	}
	// change assigment
	private void changeAssignment(){
		if(assignment1.getText().toString().isEmpty() || assignment1.getText().toString() == null){
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
				if(assignment1.getText().toString().isEmpty() || assignment1.getText().toString() == null){
					assignment = assignment1;
					assignment2.setText("");
				}else{
					assignment = assignment2;
					assignment1.setText("");
				}
				Sound.wonMinigame(getBaseContext());
				assignment.setText("Gewonnen!");
		        Settings.addScore(timeCounter);
				betweenScreen();
			}
		}else{
			t.cancel();
			assignments.clear();
		    Settings.setLives(Settings.getLives() - 1);
		    Log.i(TAG, "Minus life: lost game, " + Settings.getLives());
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
	    				if(active){
		    				timeCounter--;
		    				
		    				if(timeCounter  == 4){
		                    	Sound.countDown(getBaseContext());
		                    }
		    				
		    				if (timeCounter < 0){
		    					Sound.gameOver(getBaseContext());
		                    	Settings.setLives(Settings.getLives() - 1);
		                    	Log.i(TAG, "Minus life: out of time, " + Settings.getLives());
		    					betweenScreen();
		    					t.cancel();
	                     	}
			    		}
                 	}
	    		});

	    	}
	     }, 0, 1000);
	}
	// check swipedirection
	public boolean onTouchEvent(MotionEvent touchevent){
		switch(touchevent.getAction()){
			case MotionEvent.ACTION_DOWN:
				
				x1 = touchevent.getX();
				y1 = touchevent.getY();
				break;
			
			case MotionEvent.ACTION_UP:
				
				x2 = touchevent.getX();
				y2 = touchevent.getY();
			
				if(x == amountAssignments){
					randInt = assignments.get(arraylistKey);
				}

				if(message1.getText().toString().isEmpty() || message1.getText().toString() == null){
					message = message1;
					message2.setText("");
				}else{
					message = message2;
					message1.setText("");
				}
				
				if(randInt == 1){
					if((x1 - x2) > 180){
						swipeCorrect();
						break;
					}else{
						swipeWrong();
						break;
					}
				}
				if(randInt == 2){
					if((x1 - x2) < -180){
						swipeCorrect();
						break;
					}else{
						swipeWrong();
						break;
					}
				}
			
				if(randInt == 3){
					if((y1 - y2) > 150){
						swipeCorrect();
						break;
					}else{
						swipeWrong();
						break;
					}			
				}
				if(randInt == 4){
					if((y1 - y2) < -150){
						swipeCorrect();
						break;
					}else{
						swipeWrong();
						break;
					}
				}
				break;
			}
		return false;
	}
	
	private void swipeCorrect(){
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
	}
	
	private void swipeWrong(){
		if(x == amountAssignments){
			message.setText("Game over!");
			Sound.gameOver(getBaseContext());
			won(false);
		}else{
			message.setText(wrong);
			Sound.wrong(getBaseContext());
		}
	}
	
	private void betweenScreen(){
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent betweenPage = new Intent(this, ActivityBetween.class);
		
		betweenPage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(betweenPage != null){
			if (active){
				startActivity(betweenPage);
			}	
			this.finish();
		}
	}
}

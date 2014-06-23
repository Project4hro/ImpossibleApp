package nl.hr.impossibleapp;

import java.util.Timer;
import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.data.Sound;
import nl.hr.impossibleapp.data.Settings;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Quiz extends Activity{
	private static final String TAG = Quiz.class.toString();

    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    private int questionNumber = 0;
    private boolean good[] = {false,false,false};
    private String questions[] = {null, null, null};
    private String buttonA[] = {"B",null,"A"};
    private String buttonB[] = {"C",null,"B"};
    private String buttonC[] = {"A",null,"C"};
    private String answers[] = {"c","a","b"};
    private Button btnA;
    private Button btnB;
    private Button btnC;
    private TextView timerField;
    private Timer t = new Timer();
    private int timeCounter = 20;
	
    private boolean active = false;
    private boolean win = false;
    
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

		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_quiz);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    timerField = (TextView) findViewById(R.id.timerBox);
	    timerField.setTypeface(tf);
	    questions[0] = getResources().getString(R.string.questionOne);
	    questions[1] = getResources().getString(R.string.questionTwo);
	    questions[2] = getResources().getString(R.string.questionThree);
	    buttonA[1] = getResources().getString(R.string.questionTwoAnwerOne);
	    buttonB[1] = getResources().getString(R.string.questionTwoAnwerTwo);
	    buttonC[1] = getResources().getString(R.string.questionTwoAnwerThree);
	    int difficulty = Settings.getDifficulty();
	    if(difficulty == 3){
	    	timeCounter = 10;
	    }else if(difficulty == 2){
	    	timeCounter = 15;
	    }
	    startTimer();
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
		    				UpdateScreen();
		    				
		    				if(timeCounter  == 4){
		                    	Sound.countDown(getBaseContext());
		                    }
		    				
		    				if (timeCounter < 0){
		    					Log.i(TAG, "Out of time");
		    					gameover();
		    					Sound.gameOver(getBaseContext());
		                    	Settings.setLives(Settings.getLives() - 1);
		    					betweenScreen();
		    					t.cancel();
	                     	}
	    				}
                 	}
	    		});

	    	}
	     }, 0, 1000);
    }
    private void gameover(){
		Sound.gameOver(getBaseContext());
    	Settings.setLives(Settings.getLives() - 1);
		betweenScreen();
		t.cancel();
    }
    private void checkwin(){
    	if(good[2]){
    		Settings.addScore(timeCounter);
	        betweenScreen();
	        t.cancel();
	        Sound.removeSound();
	    	Sound.wonMinigame(getBaseContext());
	    	win = true;
    	}
    }

    private void checkAnswer(String answer){
    	if (answers[questionNumber].equals(answer)){
    		good[questionNumber] = true;
    		questionNumber++;
        	checkwin();
        	if(!win){
        		setquestion();
        	}
    	}else{
    		gameover();
    	} 	
    }
    public void ClickA(View v) {
		checkAnswer("a");
	}
    public void ClickB(View v) {
		checkAnswer("b");
	}
    public void ClickC(View v) {
		checkAnswer("c");
	}
    private void setquestion(){
    	TextView question = (TextView) findViewById(R.id.question);
    	question.setTypeface(tf);
    	question.setText(questions[questionNumber]);
    	btnA = (Button) findViewById(R.id.buttonA);
    	btnA.setText(buttonA[questionNumber]);
    	btnB = (Button) findViewById(R.id.buttonB);
    	btnB.setText(buttonB[questionNumber]);
		btnC = (Button) findViewById(R.id.buttonC);
		btnC.setText(buttonC[questionNumber]);	
    }
    public void UpdateScreen(){
	    int lives = Settings.getLives();
		ImageView heartsField = (ImageView) findViewById(R.id.hearts);
	    if(lives == 2){
	    	heartsField.setImageResource(R.drawable.heart2);
	    }else if(lives == 1){
	    	heartsField.setImageResource(R.drawable.heart1);
	    }else{
	    	heartsField.setImageResource(R.drawable.heart3);
	    }
	    TextView scoreText = (TextView) findViewById(R.id.scoreBox);
	    scoreText.setTypeface(tf);
	    scoreText.setText("Score: " + Settings.getScore());
	    
	}
    public void betweenScreen(){
		t.cancel();
		Intent between_page = new Intent(this, ActivityBetween.class);
		Sound.stopCountDown(getBaseContext());
		between_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(between_page != null){
			if (active){
				startActivity(between_page);
			}	
		}
		this.finish();
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

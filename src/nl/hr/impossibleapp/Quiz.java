package nl.hr.impossibleapp;

import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.activities.ActivityTemplate;
import nl.hr.impossibleapp.data.Sound;
import nl.hr.impossibleapp.data.Settings;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Quiz extends ActivityTemplate{
	private static final String TAG = Quiz.class.toString();

    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
    private int questionNumber = 0;
    private boolean good[] = {false,false,false};
    private String[] questions = {null, null, null};
    private String[] buttonA = {"B",null,"A"};
    private String[] buttonB = {"C",null,"B"};
    private String[] buttonC = {"A",null,"C"};
    private String[] answers = {"c","a","b"};
    private Button btnA;
    private Button btnB;
    private Button btnC;
    private TextView timerField;
	private TextView question;
    private int timeCounter = 20;
	
    private boolean win = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_quiz);
	    tf = Typeface.createFromAsset(getAssets(), FONTPATH);
	    timerField = (TextView) findViewById(R.id.timerBox);
	    timerField.setTypeface(tf);
	    questions[0] = getResources().getString(R.string.questionOne);
	    questions[1] = getResources().getString(R.string.questionTwo);
	    questions[2] = getResources().getString(R.string.questionThree);
	    buttonA[1] = getResources().getString(R.string.questionTwoAnwerOne);
	    buttonB[1] = getResources().getString(R.string.questionTwoAnwerTwo);
	    buttonC[1] = getResources().getString(R.string.questionTwoAnwerThree);
    	btnA = (Button) findViewById(R.id.buttonA);
    	btnB = (Button) findViewById(R.id.buttonB);
    	btnC = (Button) findViewById(R.id.buttonC);
    	question = (TextView) findViewById(R.id.question);
    	
    	btnA.setTypeface(tf);
    	btnB.setTypeface(tf);
    	btnC.setTypeface(tf);
    	question.setTypeface(tf);
	    int difficulty = Settings.getDifficulty();
	    if(difficulty == 3){
	    	timeCounter = 10;
	    }else if(difficulty == 2){
	    	timeCounter = 15;
	    }

	    setHeartsView((ImageView) findViewById(R.id.hearts));
	    setScoreView((TextView) findViewById(R.id.scoreBox), getAssets());
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
    public void clickA(View v) {
		checkAnswer("a");
	}
    public void clickB(View v) {
		checkAnswer("b");
	}
    public void clickC(View v) {
		checkAnswer("c");
	}
    private void setquestion(){
    	
    	question.setTypeface(tf);
    	question.setText(questions[questionNumber]);
    	btnA.setText(buttonA[questionNumber]);
    	btnB.setText(buttonB[questionNumber]);
		btnC.setText(buttonC[questionNumber]);	
    }

    public void betweenScreen(){
		t.cancel();
		Intent betweenPage = new Intent(this, ActivityBetween.class);
		Sound.stopCountDown(getBaseContext());
		betweenPage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(betweenPage != null && active){
			startActivity(betweenPage);
		}
		this.finish();
	}
}

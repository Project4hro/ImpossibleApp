package nl.hr.impossibleapp;

import java.util.Random;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.activities.ActivityTemplate;

public class QuestionGame extends ActivityTemplate{
    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
    
    private String[] questionArray = {null, null, null, null, null};
    
    private String[] question0Answers = {null, null, null};
    private String[] question1Answers = {"1", "2", "3"};
    private String[] question2Answers = {"1", "2", "3"};
    private String[] question3Answers = {"2", "3", "4"};
    private String[] question4Answers = {null, null, null};
    
    private int[] questionRightAnswers = {2, 1, 0, 2, 2};
    
    private int currentQuestionIndex = 0;
    
    private TextView textQuestionView;
    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;

    private TextView timerBox;
    private TextView scoreView;
	private ImageView heartsView;

    private int timeCounter = 10;
    
    @Override
    protected void onPause(){
    	super.onPause();
    	active = true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.layout_question_game);
        tf = Typeface.createFromAsset(getAssets(), FONTPATH);
        
	    answerButton1 = (Button) findViewById(R.id.questionAnswer1);
	    answerButton1.setTypeface(tf);
	    answerButton2 = (Button) findViewById(R.id.questionAnswer2);
	    answerButton2.setTypeface(tf);
	    answerButton3 = (Button) findViewById(R.id.questionAnswer3);
	    answerButton3.setTypeface(tf);
	    
	    currentQuestionIndex = new Random().nextInt(questionArray.length);
        setResources();
        // set answers
	    if(currentQuestionIndex == 0){
	    	answerButton1.setText(question0Answers[0]);
	    	answerButton2.setText(question0Answers[1]);
	    	answerButton3.setText(question0Answers[2]);
	    }
	    if(currentQuestionIndex == 1){
	    	answerButton1.setText(question1Answers[0]);
	    	answerButton2.setText(question1Answers[1]);
	    	answerButton3.setText(question1Answers[2]);
	    }
	    if(currentQuestionIndex == 2){
	    	answerButton1.setText(question2Answers[0]);
	    	answerButton2.setText(question2Answers[1]);
	    	answerButton3.setText(question2Answers[2]);
	    }
	    if(currentQuestionIndex == 3){
	    	answerButton1.setText(question3Answers[0]);
	    	answerButton2.setText(question3Answers[1]);
	    	answerButton3.setText(question3Answers[2]);
	    }
	    if(currentQuestionIndex == 4){
	    	answerButton1.setText(question4Answers[0]);
	    	answerButton2.setText(question4Answers[1]);
	    	answerButton3.setText(question4Answers[2]);
	    }
	    startTimer();
	    
    }
    private void startTimer(){
    	t.scheduleAtFixedRate(new TimerTask(){
	        @Override
	        public void run() {
	       	runOnUiThread(new Runnable(){
	                public void run(){
	                	timerBox.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    if(active){
		                	timeCounter--;
		                    
		                    if(timeCounter  == 4){
		                    	Sound.countDown(getBaseContext());
		                    }
		                    if (timeCounter < 0){
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
    private void setResources(){
    	question0Answers[0] = getResources().getString(R.string.black);
        question0Answers[1] = getResources().getString(R.string.orange);
        question0Answers[2] = getResources().getString(R.string.white);
        
        question4Answers[0] = getResources().getString(R.string.slingshot);
        question4Answers[1] = getResources().getString(R.string.gameboy);
        question4Answers[2] = getResources().getString(R.string.teddybear);

        questionArray[0] = getResources().getString(R.string.questionBorderColor);
        questionArray[1] = getResources().getString(R.string.questionRedHair);
        questionArray[2] = getResources().getString(R.string.questionBlondHair);
        questionArray[3] = getResources().getString(R.string.questionFlowers);
        questionArray[4] = getResources().getString(R.string.questionGirlRedHair);
        
        heartsView = (ImageView) findViewById(R.id.hearts);
        setHeartsView(heartsView);
		
		timerBox = (TextView)findViewById(R.id.timerBox);
		timerBox.setTypeface(tf);
		
		scoreView = (TextView) findViewById(R.id.scoreBox);
		setScoreView(scoreView, getAssets());
		
	    
	    textQuestionView = (TextView) findViewById(R.id.textQuestion);
	    textQuestionView.setTypeface(tf);
	    textQuestionView.setText(questionArray[currentQuestionIndex]);
    }
    // check first answer
    public void answer1(View v){
    	if(questionRightAnswers[currentQuestionIndex] == 0){
    		Sound.correct(getBaseContext());
    		betweenScreen();
    	}else{
    		wrongAnswer();
    	}
    }
    // check first answer    
    public void answer2(View v){
    	if(questionRightAnswers[currentQuestionIndex] == 1){
    		Sound.correct(getBaseContext());
    		betweenScreen();
    	}else{
    		wrongAnswer();
    	}
    }
    // check first answer
    public void answer3(View v){
    	if(questionRightAnswers[currentQuestionIndex] == 2){
    		Sound.correct(getBaseContext());
    		betweenScreen();
    	}else{
    		wrongAnswer();
    	}
    }
    
    private void wrongAnswer(){
    	Sound.gameOver(getBaseContext());
    	Settings.setLives(Settings.getLives() - 1);
    	timeCounter = 0;
    	betweenScreen();
    	t.cancel();
    }
    
    private void betweenScreen(){
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent betweenPage = new Intent(this, ActivityBetween.class);
		betweenPage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Settings.addScore(timeCounter);
		if(betweenPage != null && active){
			startActivity(betweenPage);
			finish();
		}
	}
}

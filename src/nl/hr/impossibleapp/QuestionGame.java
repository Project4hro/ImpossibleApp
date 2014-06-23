package nl.hr.impossibleapp;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import nl.hr.impossibleapp.activities.ActivityBetween;

public class QuestionGame extends Activity{
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
    private String[] questionArray = {"What was the color of the border of the picture?",
    		"How many people had red hair?",
    		"How many people had blond hair?",
    		"How many flowers where there?",
    		"What object was the girl with red hair holding?"};
    
    private String[] question0Answers = {"Black", "Orange", "White"};
    private String[] question1Answers = {"1", "2", "3"};
    private String[] question2Answers = {"1", "2", "3"};
    private String[] question3Answers = {"2", "3", "4"};
    private String[] question4Answers = {"Slingshot", "Gameboy", "Teddy Bear"};
    
    private int[] questionRightAnswers = {2, 1, 0, 2, 2};
    
    private int currentQuestionIndex = 0;
    
    private TextView textQuestionView;
    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    
	private boolean active = false;

    private TextView timerBox;

    private Timer t = new Timer();
    private int timeCounter = 10;
    
    @Override
    protected void onStart(){
    	super.onStart();
        active = true;
        Log.i("questiongame", "start");
    }
    @Override
    public void onStop(){
        super.onStop();
        active = false;
        Log.i("questiongame", "stop");
    }
    @Override
    public void onPause(){
    	super.onPause();
    	active = true;
    	Log.i("questiongame", "pause");
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.layout_question_game);
        tf = Typeface.createFromAsset(getAssets(), fontPath);
        
        ImageView heartsField = (ImageView) findViewById(R.id.hearts);
        int lives = Settings.getLives();
        if(lives == 2){
        	heartsField.setImageResource(R.drawable.heart2);
        }else if(lives == 1){
        	heartsField.setImageResource(R.drawable.heart1);
        }else{
        	heartsField.setImageResource(R.drawable.heart3);
        }
		
		timerBox = (TextView)findViewById(R.id.timerBox);
		timerBox.setTypeface(tf);
		TextView scoreText = (TextView) findViewById(R.id.scoreBox);
		scoreText.setTypeface(tf);
	    scoreText.setText("Score: " + Settings.getScore());
	    
	    currentQuestionIndex = new Random().nextInt(questionArray.length);
	    textQuestionView = (TextView) findViewById(R.id.textQuestion);
	    textQuestionView.setText(questionArray[currentQuestionIndex]);
	    answerButton1 = (Button) findViewById(R.id.questionAnswer1);
	    answerButton2 = (Button) findViewById(R.id.questionAnswer2);
	    answerButton3 = (Button) findViewById(R.id.questionAnswer3);
	    
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
		                    Log.i("timer", ""+timeCounter);
		                    
		                    if(timeCounter  == 4){
		                    	Sound.countDown(getBaseContext());
		                    }
		                    if (timeCounter < 0){
		                    	Sound.gameOver(getBaseContext());
		                    	Settings.setLives(Settings.getLives() - 1);
		                    	System.out.println("[QuestionGame] Minus life: out of time, " + Settings.getLives());
		                    	betweenScreen();
		                    	t.cancel();
		                    }
	                    }
	                 }
	            });
	        }
	    }, 0, 1000);
    }
    
    public void Answer1(View v){
    	if(questionRightAnswers[currentQuestionIndex] == 0){
    		Sound.correct(getBaseContext());
    		betweenScreen();
    	}else{
    		WrongAnswer();
    	}
    }
    
    public void Answer2(View v){
    	if(questionRightAnswers[currentQuestionIndex] == 1){
    		Sound.correct(getBaseContext());
    		betweenScreen();
    	}else{
    		WrongAnswer();
    	}
    }
    public void Answer3(View v){
    	if(questionRightAnswers[currentQuestionIndex] == 2){
    		Sound.correct(getBaseContext());
    		betweenScreen();
    	}else{
    		WrongAnswer();
    	}
    }
    
    private void WrongAnswer(){
    	Sound.gameOver(getBaseContext());
    	Settings.setLives(Settings.getLives() - 1);
    	System.out.println("[QuestionGame] Minus life: out of time, " + Settings.getLives());
    	timeCounter = 0;
    	betweenScreen();
    	t.cancel();
    }
    
    private void betweenScreen(){
    	Log.i("vraag", "colse");
		t.cancel();
		Sound.stopCountDown(getBaseContext());
		Intent between_page = new Intent(this, ActivityBetween.class);
		between_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Settings.addScore(timeCounter);
		if(between_page != null){
			if (active){
				startActivity(between_page);
				finish();
			}	
		}
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

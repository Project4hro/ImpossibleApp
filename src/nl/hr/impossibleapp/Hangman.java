package nl.hr.impossibleapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.activities.ActivityTemplate;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Hangman extends ActivityTemplate{
    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private ImageView imgHangman;
	private TextView scoreView;
	private ImageView heartsView;

	private String[] words;
	private static final String[] FOURLETTERWORDSEN = { "JAVA", "FAST", "LIFE", "WOOD", "WORK", "BEER" };
	private static final String[] FIVELETTERWORDSEN = { "CLASS", "PARSE", "LEVER", "JUICE", "EMAIL", "EIGHT" };
	private static final String[] SIXLETTERWORDSEN = { "SECOND", "MINUTE", "SENSOR", "BOTTOM", "DAMAGE", "ZOMBIE" };
	private static final String[] FOURLETTERWORDSNL = { "KLAS", "SNEL", "HOUT", "BIER", "WERK", "BEER" };
	private static final String[] FIVELETTERWORDSNL = { "LEVEN", "FRUIT", "LEVER", "NEGEN", "EMAIL", "ADRES" };
	private static final String[] SIXLETTERWORDSNL = { "SLAPEN", "MINUUT", "SENSOR", "SCHADE", "SCHOOL", "ZOMBIE" };
	
	private int currentWordIndex = 0;
	private int guessedCorrect = 0;
	private ArrayList<String> guessedLetters = new ArrayList<String>();
	private int guessedWrong = 0;
	private TextView letterBox;
    private int timeCounter = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hangman);
		tf = Typeface.createFromAsset(getAssets(), FONTPATH);
		
		Settings.setLanguage(getResources().getString(R.string.language));

		heartsView = (ImageView) findViewById(R.id.hearts);		
	    setHeartsView(heartsView);
	    
	    scoreView = (TextView) findViewById(R.id.scoreBox);
	    setScoreView(scoreView, getAssets());
	    
		getWord();
		
	    startTimer();
	}
	private void startTimer(){
		t.scheduleAtFixedRate(new TimerTask(){
	    	@Override
	    	public void run() {
	    		runOnUiThread(new Runnable(){
	    			public void run(){
	    				TextView timerField = (TextView) findViewById(R.id.timerBox);
	    				timerField.setTypeface(tf);
	    				timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	    				if(active){
	    					timeCounter--;
	    					
	    					if(timeCounter  == 4 && active){
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
	private void getWord(){
		String language = Settings.getLanguage();
		int difficulty = Settings.getDifficulty();
		if(difficulty == 1){
			if("NL".equals(language)){
				words = FOURLETTERWORDSNL;
			}else{
				words = FOURLETTERWORDSEN;
			}
		}else if(difficulty == 2){
			if("NL".equals(language)){
				words = FIVELETTERWORDSNL;
			}else{
				words = FIVELETTERWORDSEN;
			}
		}else{
			if("NL".equals(language)){
				words = SIXLETTERWORDSNL;
			}else{
				words = SIXLETTERWORDSEN;
			}
		}
		imgHangman = (ImageView) findViewById(R.id.iv_hangman);
		currentWordIndex = new Random().nextInt(words.length);
		
		if(words[currentWordIndex].length() == 3){
			findViewById(R.id.textView4).setVisibility(View.GONE);
			findViewById(R.id.textView5).setVisibility(View.GONE);
			findViewById(R.id.textView6).setVisibility(View.GONE);
		}else if(words[currentWordIndex].length() == 4){
			findViewById(R.id.textView5).setVisibility(View.GONE);
			findViewById(R.id.textView6).setVisibility(View.GONE);
		}else if(words[currentWordIndex].length() == 5){
			findViewById(R.id.textView6).setVisibility(View.GONE);
		}
	}
	
	public void insertLetter(View v){
		// display keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}
	
	private void populatePicture(int number){
		imgHangman.setImageResource(getResources().getIdentifier("drawable/hang"+number, null, getPackageName()));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode != KeyEvent.KEYCODE_BACK && keyCode != KeyEvent.KEYCODE_MENU){
			char c = (char) event.getUnicodeChar();
		    checkLetter(c);
		}
		// hide keyboard
	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
	    return super.onKeyDown(keyCode, event);
	}
	
	private void checkLetter(char letter){
		String[] separated = words[currentWordIndex].split("");
		String stringLetter = Character.toString(letter).toUpperCase(Locale.ENGLISH);
		
		if(!guessedLetters.contains(stringLetter)){
			for(int i = 1; i < (words[currentWordIndex].length() + 1); i++){
				if(separated[i].equals(stringLetter)){
					if(i == 1){
						letterBox = (TextView) findViewById(R.id.textView1);
					}else if(i == 2){
						letterBox = (TextView) findViewById(R.id.textView2);
					}else if(i == 3){
						letterBox = (TextView) findViewById(R.id.textView3);
					}else if(i == 4){
						letterBox = (TextView) findViewById(R.id.textView4);
					}else if(i == 5){
						letterBox = (TextView) findViewById(R.id.textView5);
					}else if(i == 6){
						letterBox = (TextView) findViewById(R.id.textView6);
					}
					guessedLetters.add(stringLetter);
					letterBox.setText(separated[i]);
					guessedCorrect++;
					Sound.correct(getBaseContext());
				}
			} 
		}

		if(!Arrays.asList(separated).contains(stringLetter) && !guessedLetters.contains(stringLetter)){
			guessedWrong++;
			Sound.wrong(getBaseContext());
			populatePicture(guessedWrong);
			guessedLetters.add(stringLetter);
		}
		checkWin();
	}

	private void checkWin(){
		if(guessedWrong >= 9){
			Settings.setLives(Settings.getLives() - 1);
			Sound.gameOver(getBaseContext());
			// hide keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
			active = true;
			t.cancel();
			timeCounter = 0;
			betweenScreen();
		}else if(guessedCorrect == words[currentWordIndex].length()){
			Sound.stopCountDown(getBaseContext());
	    	Sound.wonMinigame(getBaseContext());
			active = true;
			t.cancel();
			betweenScreen();
		}
	}

	private void betweenScreen(){
		t.cancel();
		Settings.addScore(timeCounter);
		Intent betweenPage = new Intent(this, ActivityBetween.class);
		Sound.stopCountDown(getBaseContext());
		betweenPage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(betweenPage != null && active){
			startActivity(betweenPage);
			this.finish();
		}
	}
}

package nl.hr.impossibleapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Hangman extends Activity{
	private static final String TAG = Hangman.class.toString();

	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	private ImageView img_hangman;
	// Words
	private String[] words;
	private final String[] four_letter_words_en = { "JAVA", "FAST", "LIFE", "WOOD", "WORK", "BEER" };
	private final String[] five_letter_words_en = { "CLASS", "PARSE", "LEVER", "JUICE", "EMAIL", "EIGHT" };
	private final String[] six_letter_words_en = { "SECOND", "MINUTE", "SENSOR", "BOTTOM", "DAMAGE", "ZOMBIE" };
	private final String[] four_letter_words_nl = { "KLAS", "SNEL", "HOUT", "BIER", "WERK", "BEER" };
	private final String[] five_letter_words_nl = { "LEVEN", "FRUIT", "LEVER", "NEGEN", "EMAIL", "ADRES" };
	private final String[] six_letter_words_nl = { "SLAPEN", "MINUUT", "SENSOR", "SCHADE", "SCHOOL", "ZOMBIE" };
	
	private int currentWordIndex = 0;
	private int guessedCorrect = 0;
	private ArrayList<String> guessedLetters = new ArrayList<String>();
	private int guessedWrong = 0;
	private static boolean active = false;
	
	//private timerField;
    private Timer t;
    private int timeCounter = 20;
	
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
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_hangman);
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		
		Settings.setLanguage(getResources().getString(R.string.language));
		// Set lives image
		int lives = Settings.getLives();
		ImageView heartsField = (ImageView) findViewById(R.id.hearts);		
	    if(lives == 2){
	    	heartsField.setImageResource(R.drawable.heart2);
	    }else if(lives == 1){
	    	heartsField.setImageResource(R.drawable.heart1);
	    }else{
	    	heartsField.setImageResource(R.drawable.heart3);
	    }
	    
	    // Set Score
	    TextView scoreText = (TextView) findViewById(R.id.scoreBox);
	    scoreText.setTypeface(tf);
	    scoreText.setText("Score: " + Settings.getScore());
	    
	    // Get language
		String language = Settings.getLanguage();
		int difficulty = Settings.getDifficulty();
		if(difficulty == 1){
			if(language.equals("NL")){
				words = four_letter_words_nl;
			}else{
				words = four_letter_words_en;
			}
		}else if(difficulty == 2){
			if(language.equals("NL")){
				words = five_letter_words_nl;
			}else{
				words = five_letter_words_en;
			}
		}else{
			if(language.equals("NL")){
				words = six_letter_words_nl;
			}else{
				words = six_letter_words_en;
			}
		}
		img_hangman = (ImageView) findViewById(R.id.iv_hangman);
		currentWordIndex = new Random().nextInt(words.length);
		System.out.println(words[currentWordIndex]);
		
		// Check word length and adapt fields
		if(words[currentWordIndex].length() == 3){
			findViewById(R.id.textView4).setVisibility(View.GONE);
			findViewById(R.id.textView5).setVisibility(View.GONE);
			findViewById(R.id.textView6).setVisibility(View.GONE);
		}else if(words[currentWordIndex].length() == 4){
			findViewById(R.id.textView5).setVisibility(View.GONE);
			findViewById(R.id.textView6).setVisibility(View.GONE);
		}else if(words[currentWordIndex].length() == 5){
			findViewById(R.id.textView6).setVisibility(View.GONE);
		}else if(words[currentWordIndex].length() == 5){
			
		}
		// Start timer
		t = new Timer();
	    t.scheduleAtFixedRate(new TimerTask() 
	    {
	    	@Override
	    	public void run() {
	    		runOnUiThread(new Runnable() 
	    		{
	    			public void run() 
	    			{
	    				TextView timerField = (TextView) findViewById(R.id.timerBox);
	    				timerField.setTypeface(tf);
	    				timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	    				timeCounter--;
	    				
	    				if(timeCounter  == 4){
	                    	Sound.countDown(getBaseContext());
	                    }
	    				
	    				if (timeCounter < 0)
	    				{
	    					Sound.gameOver(getBaseContext());
	    					timeCounter = 20;
	                    	Settings.setLives(Settings.getLives() - 1);
	                    	System.out.println("[Hangman] Minus life: out of time, " + Settings.getLives());
	                    	Log.d(TAG, "Time " + timeCounter);
	    					betweenScreen();
	    					t.cancel();
                     	}
                 	}
	    		});

	    	}
	     }, 0, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
	}
	
	// Show keyboard
	public void insertLetter(View v){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}
	
	/* pass argument number 0-9 to load picture */ 
	private void populatePicture(int number){
		img_hangman.setImageResource(getResources().getIdentifier("drawable/hang"+number, null, getPackageName()));
	}
	
	// check which key is pressed
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		char c = (char) event.getUnicodeChar();
	    checkLetter(c);
	    // Hide keyboard
	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
	    return super.onKeyDown(keyCode, event);
	}
	
	// check letter against word
	private void checkLetter(char letter){
		String[] separated = words[currentWordIndex].split("");
		String stringLetter = Character.toString(letter).toUpperCase(Locale.ENGLISH);
		
		if(!guessedLetters.contains(stringLetter)){
			// check if word contains letter is yes put it in the view
			for(int i = 1; i < (words[currentWordIndex].length() + 1); i++){
				TextView letterBox;
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
					}else{
						letterBox = (TextView) findViewById(R.id.textView6);
					}
					guessedLetters.add(stringLetter);
					letterBox.setText(separated[i]);
					guessedCorrect++;
					Sound.correct(getBaseContext());
				}
			} 
		}
		// if letter not in word add next piece to the picture
		if(!Arrays.asList(separated).contains(stringLetter) && !guessedLetters.contains(stringLetter)){
			guessedWrong++;
			Sound.wrong(getBaseContext());
			populatePicture(guessedWrong);
			guessedLetters.add(stringLetter);
		}
		checkWin();
	}
	// Check if player has won
	private void checkWin(){
		if(guessedWrong >= 9){
			Log.d(TAG, "lost");
			System.out.println("lost");
			Settings.setLives(Settings.getLives() - 1);
			Sound.gameOver(getBaseContext());
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
			active = true;
			t.cancel();
			betweenScreen();
		}else if(guessedCorrect == words[currentWordIndex].length()){
			Log.d(TAG, "won");
			System.out.println("won");
			Sound.stopCountDown(getBaseContext());
	    	Sound.wonMinigame(getBaseContext());
			active = true;
			t.cancel();
			betweenScreen();
		}
	}

	private void betweenScreen(){
		t.cancel();
		Settings.setScore(Settings.getScore() + timeCounter);
		Intent between_page = new Intent(this, ActivityBetween.class);
		Sound.stopCountDown(getBaseContext());
		
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
			t.cancel();
		    Settings.resetAll();
			this.finish();
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

package nl.hr.impossibleapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.TimerTask;

import nl.hr.impossibleapp.activities.ActivityBetween;
import nl.hr.impossibleapp.activities.ActivityTemplate;
import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.data.Sound;
import nl.hr.impossibleapp.data.Settings;

public class MemoryGame extends ActivityTemplate {	
    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
    
	// Memory Cards
    private ImageView cC;
	private int flipCard = 0;
	private boolean gotRed = false, gotYellow = false, gotGreen = false, gotBlue = false, gotWhite = false, gotPink = false;
	private int redCount, yellowCount, greenCount, blueCount, whiteCount, pinkCount;
	
	private boolean firstHit = false;
    
    private TextView timerField;
    private TextView scoreView;
	private ImageView heartsView;
	private int timeCounter = 40;
    
	//@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_memorygame);
	    int difficulty = Settings.getDifficulty();
	    if(difficulty == 3){
	    	timeCounter = 20;
	    }else if(difficulty == 2){
	    	timeCounter = 30;
	    }
	    tf = Typeface.createFromAsset(getAssets(), FONTPATH);

		timerField = (TextView) findViewById(R.id.timerBox);
		timerField.setTypeface(tf);
		heartsView = (ImageView) findViewById(R.id.hearts);
		setHeartsView(heartsView);
		
	    scoreView = (TextView) findViewById(R.id.scoreBox);
	    setScoreView(scoreView, getAssets());
	    
	    startTimer();
	}
	private void cardSwitch(View v){
		// detect which card is flipped
		if(v.getId() == R.id.card1 && redCount != 2){
			redCount++;
			flipCard++;
			cC.setImageResource(R.drawable.redcard);
		}
	    if(v.getId() == R.id.card2 && pinkCount != 2){
			pinkCount++; 
			flipCard++;
			cC.setImageResource(R.drawable.pinkcard);
	    }
	    if(v.getId() == R.id.card3 && yellowCount != 2){
    		yellowCount++; 
    		flipCard++;
    		cC.setImageResource(R.drawable.yellowcard);
    	}
	    if(v.getId() == R.id.card4 && blueCount != 2){
    		blueCount++; 
    		flipCard++;
    		cC.setImageResource(R.drawable.bluecard);
    	}
	    if(v.getId() == R.id.card5 && yellowCount != 2){
    		yellowCount++; 
    		flipCard++;
    		cC.setImageResource(R.drawable.yellowcard);
	    }
	    if(v.getId() == R.id.card6 && greenCount != 2){
    		greenCount++;
    		flipCard++;
    		cC.setImageResource(R.drawable.greencard);
    	}
	    if(v.getId() == R.id.card7 && whiteCount != 2){
    		whiteCount++; 
    		flipCard++;
    		cC.setImageResource(R.drawable.whitecard);
    	}
	    if(v.getId() == R.id.card8 && pinkCount != 2){
    		pinkCount++; 
    		flipCard++;
    		cC.setImageResource(R.drawable.pinkcard);
    	}
	    if(v.getId() == R.id.card9 && blueCount != 2){
    		blueCount++; 
    		flipCard++;
    		cC.setImageResource(R.drawable.bluecard);
    	}
	    if(v.getId() == R.id.card10 && redCount != 2){
    		redCount++; 
    		flipCard++;
    		cC.setImageResource(R.drawable.redcard);
    	}
	    if(v.getId() == R.id.card11 && greenCount != 2){
    		greenCount++;
    		flipCard++;
    		cC.setImageResource(R.drawable.greencard);
    	}
	    if(v.getId() == R.id.card12 && whiteCount != 2){
    		whiteCount++; 
    		flipCard++;
    		cC.setImageResource(R.drawable.whitecard);
    	}
	    cC.setEnabled(false);
	}
	public void cardHit(View v){
		if(firstHit){
			if (cC.isEnabled()){	
				// do nothing
			}else{
				cC.setEnabled(true);
			}
		}
		// check if cards are the same
		cC = (ImageView) findViewById(v.getId());
		if (!firstHit){
			firstHit = true;
		}
	    if(flipCard == 2){
	    	resetCards();
	    	flipCard = 0;
	    }else{
	    	cardSwitch(v);
	    }
	    if (redCount == 2 && !gotRed){
	    	gotRed=true;
	    	flipCard = 0; 
	    	Sound.correct(getBaseContext());
    	} 
	    if (yellowCount == 2 && !gotYellow){
	    	gotYellow=true; 
	    	flipCard = 0; 
	    	Sound.correct(getBaseContext());
    	} 
	    if (greenCount == 2 && !gotGreen){
	    	gotGreen=true; 
	    	flipCard = 0; 
	    	Sound.correct(getBaseContext());
    	} 
	    if (blueCount == 2 && !gotBlue){
	    	gotBlue=true; 
	    	flipCard = 0; 
	    	Sound.correct(getBaseContext());
    	} 
	    if (whiteCount == 2 && !gotWhite){
	    	gotWhite=true; 
	    	flipCard = 0; 
	    	Sound.correct(getBaseContext());
    	} 
	    if (pinkCount == 2 && !gotPink){
	    	gotPink=true; 
	    	flipCard = 0; 
	    	Sound.correct(getBaseContext());
    	}
	    
		if (gotRed && gotYellow && gotGreen && gotBlue && gotWhite && gotPink){
			betweenScreen();
		}
	}
	private void resetCards(){
		if (redCount != 2){
			cC = (ImageView) findViewById(R.id.card1); 
			cC.setImageResource(R.drawable.card);
		    cC = (ImageView) findViewById(R.id.card10); 
		    cC.setImageResource(R.drawable.card);
		    redCount = 0;
	    }
		
		if (yellowCount != 2){
			cC = (ImageView) findViewById(R.id.card3); 
			cC.setImageResource(R.drawable.card);
			cC = (ImageView) findViewById(R.id.card5); 
			cC.setImageResource(R.drawable.card); 
			yellowCount = 0;
		}

		if (greenCount != 2){
			cC = (ImageView) findViewById(R.id.card6); 
			cC.setImageResource(R.drawable.card);
 		    cC = (ImageView) findViewById(R.id.card11); 
 		    cC.setImageResource(R.drawable.card); 
 		    greenCount = 0;
	    }
						   
		if (blueCount != 2){
			cC = (ImageView) findViewById(R.id.card4); 
			cC.setImageResource(R.drawable.card);
		    cC = (ImageView) findViewById(R.id.card9); 
		    cC.setImageResource(R.drawable.card); 
		    blueCount = 0;
	    }

		if (whiteCount != 2){
			cC = (ImageView) findViewById(R.id.card7); 
			cC.setImageResource(R.drawable.card);
			cC = (ImageView) findViewById(R.id.card12); 
			cC.setImageResource(R.drawable.card); 
			whiteCount = 0;
		}

		if (pinkCount != 2){
			cC = (ImageView) findViewById(R.id.card2); 
			cC.setImageResource(R.drawable.card);
			cC = (ImageView) findViewById(R.id.card8); 
			cC.setImageResource(R.drawable.card); 
			pinkCount = 0;
		}
	}

	protected void startTimer(){ 	
	    t.scheduleAtFixedRate(new TimerTask(){
	        @Override
	        public void run() {
	            runOnUiThread(new Runnable(){
	                public void run(){
	                	timerField.setText(getResources().getString(R.string.time) + ": " + String.valueOf(timeCounter) + "s"); // you can set it to a textView to show it to the user to see the time passing while he is writing.
	                    if(active){
	                    	timeCounter--;
	                    }
	                    if(timeCounter == 4){
	                    	Sound.countDown(getBaseContext());
	                    }
	                    if (timeCounter < 0){
	                    	Sound.gameOver(getBaseContext());
	                    	Settings.setLives(Settings.getLives() - 1);
	                    	betweenScreen();
	                    	t.cancel();
	                    }
	                }
	            });
	        }
	    }, 0, 1000);
	}
	public void betweenScreen(){
		t.cancel();
		Settings.addScore(timeCounter);
		Sound.stopCountDown(getBaseContext());
		Intent betweenPage = new Intent(this, ActivityBetween.class);
		if(betweenPage != null && active){
			startActivity(betweenPage);
		}
		this.finish();
	}
}


	
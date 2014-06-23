package nl.hr.impossibleapp.activities;

import com.parse.Parse;
import com.parse.ParseObject;

import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.data.Highscores;
import nl.hr.impossibleapp.data.MySQLiteHelper;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ActivityEndScreen extends Activity{
	private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		Parse.initialize(this, "w7caHYa8ssCTvQrbDEvYgR4U0JeUunBylIwMZ7sU", "rEOP8nJx28SjmGFBrfYXT96LgSMH5Dq77jJ66X0W");
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_endscreen);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    
	    TextView scoreView = (TextView) findViewById(R.id.textView1);
	    scoreView.setTypeface(tf);
	    scoreView.setText("Je score is: " + Settings.getScore());
	    if(Settings.getScore() != 0){
		    String name = Settings.getName();
		    int score = Settings.getScore();
		    MySQLiteHelper db = new MySQLiteHelper(this);
		    db.addHighscore(new Highscores(name, score));
		    // Save online		    
		    ParseObject highscores = new ParseObject("highscores");
			highscores.put("Name", name);
			highscores.put("Score", score);
			highscores.saveInBackground();
	    }
	    
	    if(Settings.getLives() != 0){
	    	Sound.wonAll(getBaseContext());
	    }else{
	    	Sound.endGameOutOfLives(getBaseContext());
	    }
	    Settings.resetAll();
	}
	
	public void Goto_Menu(View v){
		this.finish();
	}
}

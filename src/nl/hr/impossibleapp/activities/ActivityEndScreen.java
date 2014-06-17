package nl.hr.impossibleapp.activities;

import java.util.List;

import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.data.Highscores;
import nl.hr.impossibleapp.data.MySQLiteHelper;
import nl.hr.impossibleapp.data.Settings;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class ActivityEndScreen extends Activity{
	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_endscreen);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    
	    TextView score = (TextView) findViewById(R.id.textView1);
	    score.setTypeface(tf);
	    score.setText("Je score is: " + Settings.getScore());
	    
	    MySQLiteHelper db = new MySQLiteHelper(this);
	    db.addHighscore(new Highscores(Settings.getName(), Settings.getScore()));
	    List<Highscores> list = db.getAllHighscores();
	    
	    System.out.println("Highscore - name: " + list.get(0).getName());
	    System.out.println("Highscore - score: " + list.get(0).getScore());
	    
	    Settings.resetAll();
	}
	
	public void Goto_Menu(View v){
		Intent menu = new Intent(this, ActivityMenu.class);
		menu.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(menu != null){
			startActivity(menu);
			this.finish();
		}
	}
}

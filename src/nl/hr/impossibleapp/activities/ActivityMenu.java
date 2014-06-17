package nl.hr.impossibleapp.activities;

import nl.hr.impossibleapp.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ActivityMenu extends Activity
{
	private static final String fontPath = "fonts/mvboli.ttf";
	private static final String TAG = ActivityMenu.class.toString();
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_menu);
		
		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        Button start_button = (Button) findViewById(R.id.menu_start);
        Button highscores_button = (Button) findViewById(R.id.menu_score);
        Button settings_button = (Button) findViewById(R.id.menu_settings);
        if(start_button != null){
        	start_button.setTypeface(tf);
        }
        if(highscores_button != null){
        	highscores_button.setTypeface(tf);
        }
        if(settings_button != null){
        	settings_button.setTypeface(tf);
        }
        
		Log.i(TAG, "inLayout - finished"); // werkt
        Log.d(TAG, "Geladen"); // werkt niet
	}
	
	public void Goto_Settings(View v){
		Intent settings_page = new Intent(this, ActivitySettings.class);
		settings_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(settings_page != null){
			startActivity(settings_page);
		}
	}
	
	public void Goto_Highscores(View v){
		Intent highscores_page = new Intent(this, ActivityHighscores.class);
		highscores_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(highscores_page != null){
			startActivity(highscores_page);
		}
	}
	
	public void Goto_Game(View v){
		Intent game_page = new Intent(this, ActivityBetween.class);
		game_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(game_page != null){
			startActivity(game_page);
		}
	}
}
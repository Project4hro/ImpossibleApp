package nl.hr.impossibleapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Activity_Menu extends Activity
{
	private int currentGame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    // Font path
	    String fontPath = "fonts/mvboli.ttf";
		setContentView(R.layout.layout_menu);
		
		// text button
        Button start_button = (Button) findViewById(R.id.menu_start);
        Button highscores_button = (Button) findViewById(R.id.menu_score);
        Button settings_button = (Button) findViewById(R.id.menu_settings);
        
		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		
		// Applying font
        start_button.setTypeface(tf);
        highscores_button.setTypeface(tf);
        settings_button.setTypeface(tf);
	}
	
	public void Goto_Settings(View v)
	{
		Intent settings_page = new Intent(this, Activity_Settings.class);
		settings_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(settings_page != null){
			startActivity(settings_page);
		}
	}
	public void Goto_Game(View v)
	{
		Intent game_page = new Intent(this, Activity_Between.class);
		game_page.putExtra("intCurrentGame", currentGame);
		game_page.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(game_page != null){
			startActivity(game_page);
		}
	}
	@Override
	public void onBackPressed() {finish();}
}
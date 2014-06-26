package nl.hr.impossibleapp.activities;

import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.data.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class ActivityMenu extends Activity{
	private static final String FONTPATH = "fonts/mvboli.ttf";
	private static final String TAG = ActivityMenu.class.toString();
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_menu);
		
		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), FONTPATH);

        Button startButton = (Button) findViewById(R.id.menu_start);
        Button highscoresButton = (Button) findViewById(R.id.menu_score);
        Button settingsButton = (Button) findViewById(R.id.menu_settings);
        if(startButton != null){
        	startButton.setTypeface(tf);
        }
        if(highscoresButton != null){
        	highscoresButton.setTypeface(tf);
        }
        if(settingsButton != null){
        	settingsButton.setTypeface(tf);
        }
        
		Log.i(TAG, "inLayout - finished"); // werkt
		// get sharedpreferences
        prefs = this.getSharedPreferences("nl.hr.impossibleapp", Context.MODE_PRIVATE);
        Settings.setSoundEnabled(prefs.getBoolean("Sound", true));
        Settings.setName(prefs.getString("Username", null));
        if(Settings.getName() == null){
        	showPopup();
        }
        
	}
	
	public void gotoSettings(View v){
		Intent settingsPage = new Intent(this, ActivitySettings.class);
		settingsPage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(settingsPage != null){
			startActivity(settingsPage);
		}
	}
	
	public void gotoHighscores(View v){
		Intent highscoresPage = new Intent(this, ActivityHighscores.class);
		highscoresPage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(highscoresPage != null){
			startActivity(highscoresPage);
		}
	}
	
	public void gotoGame(View v){
		Intent betweenPage = new Intent(this, ActivityBetween.class);
		betweenPage.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(betweenPage != null){
			startActivity(betweenPage);
		}
	}
	private void showPopup(){
		final EditText playerName = new EditText(this);
    	playerName.setHint(getResources().getString(R.string.username));
    	new AlertDialog.Builder(this)
    	.setTitle(getResources().getString(R.string.insertUsername))
    	.setMessage(getResources().getString(R.string.insertUsername))
    	.setView(playerName)
    	.setPositiveButton("Save",new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			String name = playerName.getText().toString();
	   
	    		if(name == null || name.length() == 0 || name.isEmpty()){
	    			showPopup();
	    			Log.i(TAG, "Name" + name + "End");
	    		}else{
	    			saveName(name);
	    		}
	    		
    		}
		}).show();
	}
	private void saveName(String name){
		Log.i(TAG, name);
		Settings.setName(name);
		prefs.edit().putString("Username", name).commit();
	}
}
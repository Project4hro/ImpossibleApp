package nl.hr.impossibleapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Activity_Settings extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	// Font path
	    String fontPath = "fonts/mvboli.ttf";
	    setContentView(R.layout.layout_settings);
	    
	    // text button
        Button menu_button = (Button) findViewById(R.id.settings_backButton);
        
		// Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		
		// Applying font
        menu_button.setTypeface(tf);
        
        
	}
	public void Goto_Menu(View v){
		//Activity_settings.this.finish();
		Intent menu_page = new Intent(this, Activity_Menu.class);
		if(menu_page != null){
			this.finish();
		}
	}
}

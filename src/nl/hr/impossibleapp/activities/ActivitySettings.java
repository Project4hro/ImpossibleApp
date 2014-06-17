package nl.hr.impossibleapp.activities;

import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.data.Settings;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class ActivitySettings extends Activity{
	private static final String fontPath = "fonts/mvboli.ttf"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_settings);

		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
	    
        Button menu_button = (Button) findViewById(R.id.settings_backButton);
        menu_button.setTypeface(tf);
        
        Switch toggle = (Switch) findViewById(R.id.settings_switch1);
        toggle.setChecked(Settings.isSoundEnabled());
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                	Settings.setSoundEnabled(true);
                	System.out.println("aan");
                } else {
                    // The toggle is disabled
                	Settings.setSoundEnabled(false);
                	System.out.println("uit");
                }
            }
        });
    }
	
	public void Goto_Menu(View v){
		this.finish();
	}
}

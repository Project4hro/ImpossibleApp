package nl.hr.impossibleapp.activities;

import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.data.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class ActivitySettings extends Activity{
	private static final String fontPath = "fonts/mvboli.ttf";
	//private static final String TAG = ActivitySettings.class.toString(); 
	private EditText editName;
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_settings);
	    prefs = this.getSharedPreferences("nl.hr.impossibleapp", Context.MODE_PRIVATE);
	    
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
	    
        Button menu_button = (Button) findViewById(R.id.settings_backButton);
        menu_button.setTypeface(tf);
        editName = (EditText) findViewById(R.id.editName);
        editName.setText(Settings.getName());
        editName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        Switch toggle = (Switch) findViewById(R.id.settings_switch1);
        toggle.setChecked(Settings.isSoundEnabled());
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSound(isChecked);
            }
        });
    }
	
	public void Goto_Menu(View v){
		saveName();
		this.finish();
	}
	
	private void saveSound(boolean soundEnabled){
		Settings.setSoundEnabled(soundEnabled);
		prefs.edit().putBoolean("Sound", soundEnabled).commit();
	}
	
	public void saveName(){
		Settings.setName(editName.getText().toString());
		prefs.edit().putString("Username", editName.getText().toString()).commit();
	}
	@Override
	public void onBackPressed(){
		saveName();
		this.finish();
	}
}

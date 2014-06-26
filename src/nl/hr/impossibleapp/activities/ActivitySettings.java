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
import android.widget.TextView;

public class ActivitySettings extends Activity{
	private static final String FONTPATH = "fonts/mvboli.ttf";
	private Typeface tf;
	private EditText editName;
	private SharedPreferences prefs;
	private Button menuButton;
	private TextView nameLabel;
	private Switch toggleSound;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.layout_settings);
	    // Load sharedpreferences
	    prefs = this.getSharedPreferences("nl.hr.impossibleapp", Context.MODE_PRIVATE);
	    
		tf = Typeface.createFromAsset(getAssets(), FONTPATH);
	    
        menuButton = (Button) findViewById(R.id.settings_backButton);
        menuButton.setTypeface(tf);
        editName = (EditText) findViewById(R.id.editName);
        editName.setTypeface(tf);
        editName.setText(Settings.getName());
        editName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        nameLabel = (TextView) findViewById(R.id.textView1);
        nameLabel.setTypeface(tf);
        toggleSound = (Switch) findViewById(R.id.settings_switch1);
        toggleSound.setTypeface(tf);
        toggleSound.setChecked(Settings.isSoundEnabled());
        toggleSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveSound(isChecked);
            }
        });
    }
	
	public void gotoMenu(View v){
		saveName();
		this.finish();
	}
	// Save sound to sharedpreferences
	private void saveSound(boolean soundEnabled){
		Settings.setSoundEnabled(soundEnabled);
		prefs.edit().putBoolean("Sound", soundEnabled).commit();
	}
	// Save name to sharedpreferences	
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

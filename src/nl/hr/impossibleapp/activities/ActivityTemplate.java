package nl.hr.impossibleapp.activities;

import java.util.Timer;

import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.data.Settings;
import nl.hr.impossibleapp.data.Sound;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityTemplate extends Activity{
	private static final String FONTPATH = "fonts/mvboli.ttf";
    private static Typeface tf;
	protected Timer t = new Timer();
	protected boolean active = false;
	
	@Override
	protected void onStart(){
		super.onStart();
       	active = true;
	}
	
	@Override
	protected void onStop(){
		super.onStop();
        active = false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Set exit button
	    menu.add("Exit"); 
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if ("Exit".equals(item.getTitle())){
			t.cancel();
			Sound.stopCountDown(getBaseContext());
    		Settings.resetAll();
		    this.finish();
		}
	    return super.onOptionsItemSelected(item);    
	}
	
	@Override
	public void onBackPressed(){
		// prevent that minigame will be ended accidently
	}
	
	// Set scoreView
	public static void setScoreView(TextView score, AssetManager context){
		tf = Typeface.createFromAsset(context, FONTPATH);
		score.setTypeface(tf);
		score.setText("Score: " + Settings.getScore());
	}
	// Set lives view
	public static void setHeartsView(ImageView hearts){
		if(Settings.getLives() == 1){
			hearts.setImageResource(R.drawable.heart1);
		}else if(Settings.getLives() == 2){
			hearts.setImageResource(R.drawable.heart2);
		}else if(Settings.getLives() == 3){
			hearts.setImageResource(R.drawable.heart3);
		}
	}
}

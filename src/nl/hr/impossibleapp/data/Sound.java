package nl.hr.impossibleapp.data;

import nl.hr.impossibleapp.R;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

public class Sound extends Activity
{
	static MediaPlayer mp;
	static MediaPlayer end;
	int timerInt;
	
	public static MediaPlayer removeSound(){
		if(mp != null)
		{
			mp.release();
		}
		return mp = null;
	}
	
	public static MediaPlayer correct(Context context){
		removeSound();
		mp = MediaPlayer.create(context, R.raw.ding);
		if(Settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
	public static MediaPlayer wrong(Context context){
		removeSound();
		mp = MediaPlayer.create(context, R.raw.wrong);
		if(Settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
	public static MediaPlayer gameOver(Context context){
		removeSound();
		mp = MediaPlayer.create(context, R.raw.fail);
		if(Settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
	public static MediaPlayer countDown(Context context){
		if(end != null)
		{
			end.release();
		}
		end = MediaPlayer.create(context, R.raw.countdown);
		if(Settings.isSoundEnabled()){
			end.start();
		}
		return end;
	}
	
	public static MediaPlayer stopCountDown(Context context){
		if(end != null)
		{
			end.release();
		}
		return end;
	}
	
	public static MediaPlayer shortCountDown(Context context){
		if(end!= null)
		{
			end.release();
		}
		end = MediaPlayer.create(context, R.raw.shortcountdown);
		if(Settings.isSoundEnabled()){
			end.start();
		}
		return end;
	}
	
	public static MediaPlayer Ready(Context context){
		removeSound();
		mp = MediaPlayer.create(context, R.raw.ready);
		if(Settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
	public static MediaPlayer Go(Context context){
		removeSound();
		mp = MediaPlayer.create(context, R.raw.go);
		if(Settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
	public static MediaPlayer wonMinigame(Context context){
		removeSound();
		mp = MediaPlayer.create(context, R.raw.complete);
		if(Settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
}

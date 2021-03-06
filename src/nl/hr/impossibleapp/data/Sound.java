package nl.hr.impossibleapp.data;

import nl.hr.impossibleapp.R;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

public class Sound extends Activity{
	static MediaPlayer mp;
	static MediaPlayer end;
	
	public static MediaPlayer removeSound(){
		if(mp != null){
			mp.release();
		}
		mp = null;
		return mp;
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
		if(end != null){
			end.release();
		}
		end = MediaPlayer.create(context, R.raw.countdown);
		if(Settings.isSoundEnabled()){
			end.start();
		}
		return end;
	}
	
	public static MediaPlayer stopCountDown(Context context){
		if(end != null){
			end.release();
		}
		return end;
	}
	
	public static MediaPlayer shortCountDown(Context context){
		if(end!= null){
			end.release();
		}
		end = MediaPlayer.create(context, R.raw.shortcountdown);
		if(Settings.isSoundEnabled()){
			end.start();
		}
		return end;
	}
	
	public static MediaPlayer ready(Context context){
		removeSound();
		mp = MediaPlayer.create(context, R.raw.ready);
		if(Settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
	public static MediaPlayer go(Context context){
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
	
	public static MediaPlayer endGameOutOfLives(Context context){
		removeSound();
		mp = MediaPlayer.create(context, R.raw.lostgame);
		if(Settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
	public static MediaPlayer wonAll(Context context){
		removeSound();
		mp = MediaPlayer.create(context, R.raw.wonall);
		if(Settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
}

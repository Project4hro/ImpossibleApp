package nl.hr.impossibleapp;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.SoundEffectConstants;

public class Sound extends Activity
{
	static MediaPlayer mp;
	static MediaPlayer end;
	int timerInt;
	
	public static MediaPlayer removeSound()
	{
		if(mp!= null)
		{
			mp.release();
		}
		return mp = null;
	}
	
	public static MediaPlayer correct(Context context)
	{
		removeSound();
		mp = MediaPlayer.create(context, R.raw.ding);
		if(settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
	public static MediaPlayer wrong(Context context)
	{
		removeSound();
		mp = MediaPlayer.create(context, R.raw.wrong);
		if(settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
	public static MediaPlayer gameOver(Context context)
	{
		removeSound();
		mp = MediaPlayer.create(context, R.raw.fail);
		if(settings.isSoundEnabled()){
			mp.start();
		}
		return mp;
	}
	
	public static MediaPlayer countDown(Context context)
	{
		if(end!= null)
		{
			end.release();
		}
		end = MediaPlayer.create(context, R.raw.countdown);
		if(settings.isSoundEnabled()){
			end.start();
		}
		return end;
	}
	
}

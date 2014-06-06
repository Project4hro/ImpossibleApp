package nl.hr.impossibleapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class TurnActivity extends Activity{
	private static boolean active = true;
	private float currentRotation[] = {0,0,0,0,0,0,0,0,0,0,0,0};
	private ImageView img;
	private int win = 3;
    Timer t;
    int TimeCounter = 20;
    TextView timerField;
    @Override
    public void onStart() 
    {
       super.onStart();
       active = true;
    } 

    @Override
    public void onStop() 
    {
        super.onStop();
        active = false;
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_turn);
		
		/*// Font path
	    String fontPath = "fonts/mvboli.ttf";
	    // Loading Font Face
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);*/
		//RelativeLayout relative = (RelativeLayout) findViewById(R.id.RelativeLayout1);
		timerField = (TextView) findViewById(R.id.timerBox1);
		if(timerField == null){
			System.out.println("timer leeg");
		}
		//timerField.setTypeface(tf);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		t = new Timer();
	    t.scheduleAtFixedRate(new TimerTask() 
	    
	    {
	    	@Override
	    	public void run() {
	    		runOnUiThread(new Runnable() 
	    		{
	    			public void run() 
	    			{
	    				//timerField.setText(String.valueOf(TimeCounter)); 
	    				//timerField.setText("hoi");
	    				// you can set it to a textView to show it to the user to see the time passing while he is writing.
	    				TimeCounter--;
	    				
	    				
	    				if (TimeCounter < 1)
	    				{
	    					TimeCounter = 20;
	                    	settings.setLives(settings.getLives() - 1);
	                    	System.out.println("[TurnActivity] Minus life: out of time, " + settings.getLives());
	    					betweenScreen();
	    					t.cancel();
                     	}
                 	}
	    		});

	    	}
	     }, 0, 1000); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.

	}
	public void Make_Turn(int id){
		
		RotateAnimation anim = new RotateAnimation(currentRotation[id], currentRotation[id] + 90,
	            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
	    currentRotation[id] = (currentRotation[id] + 90) % 360;
	    anim.setInterpolator(new LinearInterpolator());
	    anim.setDuration(100);
	    anim.setFillEnabled(true);
	    anim.setFillAfter(true);
	    img.startAnimation(anim);
	    changeturns(id, (int) currentRotation[id]);
	    win();
	}
	private void changeturns(int id, int currentRotation2) {
		if(currentRotation2 == 90){
		
			if (id == 4 || id == 5 || id == 2 || id == 6 || id == 0 ){
				win +=1;
			}
			else if (id == 9 || id == 1 || id == 3 ){
				win = win -1;
			}
		}
		else if (currentRotation2 == 180){
			if (id == 9 || id == 7 || id == 3){
				win +=1;
			}
			else if (id == 4 || id == 5 || id == 2 || id == 6 || id == 0 ){
				win = win -1;
			}
		}
		else if (currentRotation2 == 270){
			if (id == 4 || id == 5 || id == 2 || id == 10 || id == 0 ){
				win +=1;
			}
			else if (id == 9 || id == 7 || id == 3){
				win = win -1;
			}
		}
		else if (currentRotation2 == 0){
			if (id == 9 || id == 1){
				win +=1;
			}
			else if (id == 4 || id == 5 || id == 2 || id == 10 || id == 0 ){
				win = win -1;
			}
		}
	}
		
		
	public void Turn_Img0(View v) {
		img = (ImageView) findViewById(R.id.imageView0);
		Make_Turn(0);
		
	}
	public void Turn_Img1(View v) {
		img = (ImageView) findViewById(R.id.imageView1);
		Make_Turn(1);
	}
	public void Turn_Img2(View v) {
		img = (ImageView) findViewById(R.id.imageView2);
		Make_Turn(2);
	}
	public void Turn_Img3(View v) {
		img = (ImageView) findViewById(R.id.imageView3);
		Make_Turn(3);
	}
	public void Turn_Img4(View v) {
		img = (ImageView) findViewById(R.id.imageView4);
		Make_Turn(4);
	}
	public void Turn_Img5(View v) {
		img = (ImageView) findViewById(R.id.imageView5);
		Make_Turn(5);
	}
	public void Turn_Img6(View v) {
		img = (ImageView) findViewById(R.id.imageView6);
		Make_Turn(6);
	}
	public void Turn_Img7(View v) {
		img = (ImageView) findViewById(R.id.imageView7);
		Make_Turn(7);
	}
	public void Turn_Img8(View v) {
		img = (ImageView) findViewById(R.id.imageView8);
		Make_Turn(8);
	}
	public void Turn_Img9(View v) {
		img = (ImageView) findViewById(R.id.imageView9);
		Make_Turn(9);
	}
	public void Turn_Img10(View v) {
		img = (ImageView) findViewById(R.id.imageView10);
		Make_Turn(10);
	}
	public void Turn_Img11(View v) {
		img = (ImageView) findViewById(R.id.imageView11);
		Make_Turn(11);
	}
	
	public void win(){
		if (win == 10){
			betweenScreen();
			
		}
	}
	public void betweenScreen()
	{
		t.cancel();
		//Intent mIntent = getIntent();
		//int currentGame = mIntent.getIntExtra("intCurrentGame", 0);
		Intent between_page = new Intent(this, Activity_Between.class);
		//between_page.putExtra("intCurrentGame", currentGame);
		//between_page.putExtra("intCurrentGame", 0);
		int score = settings.getScore() + TimeCounter;
        settings.setScore(score);
		if(between_page != null){
			if (active)
			{
				startActivity(between_page);
			}
			this.finish();	
		}
	}

	//Eventlistener that checks if menu button is pressed
		@Override
		public boolean onCreateOptionsMenu(Menu menu) 
		{
			// adds exit button
		    menu.add("Exit"); 
		    return super.onCreateOptionsMenu(menu);
		}
		//Eventlistener that checks if user presses exit
		@Override
		public boolean onOptionsItemSelected(MenuItem item) 
		{
		    // If exit    
		    if (item.getTitle() == "Exit") //user clicked Exit
				t.cancel();
		    	this.finish(); //will call onPause
		    return super.onOptionsItemSelected(item);    
		}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_turn, container,
					false);
			return rootView;
		}
	}

}

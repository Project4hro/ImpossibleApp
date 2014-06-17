package nl.hr.impossibleapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class Activity_Highscores extends Activity{
	private static final String TAG = Activity_Highscores.class.toString();
	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    private TableLayout tl;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// Full Screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_highscores);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    // Set table headers
	    tl = (TableLayout) findViewById(R.id.main_table);
	    TableRow tr_head = new TableRow(this);
	    tr_head.setId(10);
	    tr_head.setBackgroundColor(Color.GRAY);
	    tr_head.setLayoutParams(new LayoutParams(
	    LayoutParams.WRAP_CONTENT,
	    LayoutParams.WRAP_CONTENT));
	    // set header name
	    TextView label_name = new TextView(this);
	    label_name.setId(20);
	    label_name.setText("Name");
	    label_name.setTextColor(Color.WHITE);
	    label_name.setPadding(10, 5, 5, 5);
	    label_name.setTypeface(tf);
	    label_name.setTextAppearance(this, R.style.text_style);
        tr_head.addView(label_name);// add the column to the table row here
        // set header score
        TextView label_score = new TextView(this);
        label_score.setId(21);// define id that must be unique
        label_score.setText("Score"); // set the text for the header 
        label_score.setTextColor(Color.WHITE); // set the color
        label_score.setPadding(2, 5, 5, 5); // set the padding (if required)
        label_score.setTypeface(tf);
        label_score.setTextAppearance(this, R.style.text_style);
        tr_head.addView(label_score); // add the column to the table row here
	    
        tl.addView(tr_head, new TableLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        
	    MySQLiteHelper db = new MySQLiteHelper(this);	    
	    List<Highscores> list = db.getAllHighscores();
	    
	    if(list.isEmpty()){
	    	Log.d(TAG, "Highscores is leeg");
	    }
	    // Go over all results
	    for(int i = 0; i < list.size(); i++){
	    	// create new tablerow
	    	TableRow tr = new TableRow(this);
	    	tr.setId(100+i);
	    	tr.setLayoutParams(new LayoutParams(
	    	LayoutParams.WRAP_CONTENT,
	    	LayoutParams.WRAP_CONTENT));
	    	
	    	TextView labelName = new TextView(this);
	    	labelName.setId(200 + i);
	    	labelName.setText(list.get(i).getName());
	    	labelName.setPadding(10, 0, 5, 0);
	    	labelName.setTextColor(Color.WHITE);
	    	labelName.setTypeface(tf);
	    	labelName.setTextAppearance(this, R.style.text_style);
	    	tr.addView(labelName);
	    	
	    	TextView labelScore = new TextView(this);
	    	labelScore.setId(200 + i);
	    	labelScore.setText(Integer.toString(list.get(i).getScore()));
	    	labelScore.setPadding(2, 0, 5, 0);
	    	labelScore.setTextColor(Color.WHITE);
	    	labelScore.setTypeface(tf);
	    	labelScore.setTextAppearance(this, R.style.text_style);
	    	tr.addView(labelScore);
	    	// add tablerow to table
	    	tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
	    }
	}
	// button menu
	public void Goto_Menu(View v){
		Intent menu = new Intent(this, Activity_Menu.class);
		menu.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(menu != null){
			startActivity(menu);
		}
		this.finish();
	}
}

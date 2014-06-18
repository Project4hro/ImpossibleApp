package nl.hr.impossibleapp.activities;

import java.util.List;

import nl.hr.impossibleapp.R;
import nl.hr.impossibleapp.data.Highscores;
import nl.hr.impossibleapp.data.MySQLiteHelper;
import android.app.Activity;
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

public class ActivityHighscores extends Activity{
	private static final String TAG = ActivityHighscores.class.toString();
	// Font
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    private TableLayout tl;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_highscores);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    
	    tl = (TableLayout) findViewById(R.id.main_table);
	    TableRow tr_head = new TableRow(this);
	    tr_head.setId(10);
	    tr_head.setBackgroundColor(Color.GRAY);
	    tr_head.setLayoutParams(new LayoutParams(
	    LayoutParams.WRAP_CONTENT,
	    LayoutParams.WRAP_CONTENT));
	    
	    TextView label_name = new TextView(this);
	    label_name.setId(20);
	    label_name.setText("Name");
	    label_name.setTextColor(Color.WHITE);
	    label_name.setPadding(10, 5, 5, 5);
	    label_name.setTypeface(tf);
	    label_name.setTextAppearance(this, R.style.text_style);
        tr_head.addView(label_name);
        
        TextView label_score = new TextView(this);
        label_score.setId(21);
        label_score.setText("Score"); 
        label_score.setTextColor(Color.WHITE);
        label_score.setPadding(2, 5, 5, 5);
        label_score.setTypeface(tf);
        label_score.setTextAppearance(this, R.style.text_style);
        tr_head.addView(label_score);
	    
        tl.addView(tr_head, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addHighscores();
	}
	
	private void addHighscores(){
		MySQLiteHelper db = new MySQLiteHelper(this);	    
	    List<Highscores> list = db.getAllHighscores();
	    
	    if(list.isEmpty()){
	    	Log.i(TAG, "Highscores is leeg");
	    }
	    for(int i = 0; i < list.size(); i++){
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
	    	
	    	tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    }
	}
	
	public void GotoMenu(View v){
		Log.i(TAG, "Menu");
		this.finish();
	}
}

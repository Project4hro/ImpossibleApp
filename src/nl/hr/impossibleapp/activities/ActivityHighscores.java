package nl.hr.impossibleapp.activities;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class ActivityHighscores extends Activity{
	private static final String TAG = ActivityHighscores.class.toString();
	// Font
    private static final String FONTPATH = "fonts/mvboli.ttf";
    private Typeface tf;
    private TableLayout tl;
    private TextView errMsg;
	private List<ParseObject> scores;
    private int currentView = 1;
    private Button highscoresBtn;
    private Button menuBtn;
    private String name;
    private int score;
    private String local;
    private String worldwide;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		Parse.initialize(this, "w7caHYa8ssCTvQrbDEvYgR4U0JeUunBylIwMZ7sU", "rEOP8nJx28SjmGFBrfYXT96LgSMH5Dq77jJ66X0W");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_highscores);
	    
	    tf = Typeface.createFromAsset(getAssets(), FONTPATH);
	    errMsg = (TextView) findViewById(R.id.errMessage);
	    errMsg.setTypeface(tf);
	    menuBtn = (Button) findViewById(R.id.menuBtn);
	    menuBtn.setTypeface(tf);
	    worldwide = getResources().getString(R.string.worldwide);
	    local = getResources().getString(R.string.local);
		highscoresBtn = (Button) findViewById(R.id.highscoresBtn);
		highscoresBtn.setTypeface(tf);
		highscoresBtn.setText(worldwide);
		
        addLocalHighscores();
	}
	
	private void addLocalHighscores(){
		errMsg.setVisibility(View.VISIBLE);
		if(currentView == 2){
			tl.removeAllViews();
			currentView = 1;
		}
		// get top 10 scores locally stored
		addTableHeader();
		MySQLiteHelper db = new MySQLiteHelper(this);	    
	    List<Highscores> list = db.getAllHighscores();
	    
	    if(list.isEmpty()){
	    	Log.i(TAG, "Highscores is leeg");
	    	errMsg.setText("Geen resultaten");
	    }else{
	    	errMsg.setVisibility(View.GONE);
	    }
	    for(int i = 0; i < list.size(); i++){
	    	name = list.get(i).getName();
			score = list.get(i).getScore();
			addRow(i);
	    }
	}
	
	private void getOnlineHighscores(){
		errMsg.setVisibility(View.VISIBLE);
		tl.removeAllViews();
		// get top 10 scores stored online
		ParseQuery<ParseObject> query = ParseQuery.getQuery("highscores");
		query.orderByDescending("Score");
		query.setLimit(10);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		            Log.i(TAG, "Retrieved " + scoreList.size() + " scores");
		            scores = scoreList;
		            if(scoreList.isEmpty()){
		            	displayErrorMessage();
		            }else{
		            	errMsg.setVisibility(View.GONE);
		            	displayOnlineScores();
		            }
		        } else {
		            Log.i(TAG, "Error: " + e.getMessage());
		            displayErrorMessage();
		        }
				currentView = 2;
		    }
		});
	}
	
	public void displayErrorMessage(){
		errMsg.setText(getResources().getString(R.string.noResults));
		errMsg.setTypeface(tf);
	}
	
	private void displayOnlineScores(){
		addTableHeader();
		for(int i = 0; i < scores.size(); i++){
			name = scores.get(i).getString("Name");
			score = scores.get(i).getInt("Score");
			addRow(i);
		}
	}
	
	private void addTableHeader(){
		tl = (TableLayout) findViewById(R.id.main_table);
	    TableRow trHead = new TableRow(this);
	    trHead.setId(10);
	    trHead.setBackgroundColor(Color.GRAY);
	    trHead.setLayoutParams(new LayoutParams(
	    LayoutParams.WRAP_CONTENT,
	    LayoutParams.WRAP_CONTENT));
	    
	    TextView labelPosition = new TextView(this);
	    labelPosition.setId(20);
	    labelPosition.setText("#");
	    labelPosition.setTextColor(Color.WHITE);
	    labelPosition.setPadding(10, 5, 5, 5);
	    labelPosition.setTypeface(tf);
	    labelPosition.setTextAppearance(this, R.style.text_style);
        trHead.addView(labelPosition);
        
	    TextView labelName = new TextView(this);
	    labelName.setId(20);
	    labelName.setText("Name");
	    labelName.setTextColor(Color.WHITE);
	    labelName.setPadding(2, 5, 5, 5);
	    labelName.setTypeface(tf);
	    labelName.setTextAppearance(this, R.style.text_style);
        trHead.addView(labelName);
        
        TextView labelScore = new TextView(this);
        labelScore.setId(21);
        labelScore.setText("Score"); 
        labelScore.setTextColor(Color.WHITE);
        labelScore.setPadding(2, 5, 5, 5);
        labelScore.setTypeface(tf);
        labelScore.setTextAppearance(this, R.style.text_style);
        trHead.addView(labelScore);
	    
        tl.addView(trHead, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	private void addRow(int i){
		TableRow tr = new TableRow(this);
    	tr.setId(100+i);
    	tr.setLayoutParams(new LayoutParams(
    	LayoutParams.WRAP_CONTENT,
    	LayoutParams.WRAP_CONTENT));
    	
    	TextView positionLabel = new TextView(this);
    	positionLabel.setId(200 + i);
    	positionLabel.setText("" + (i + 1));
    	positionLabel.setPadding(10, 0, 5, 0);
    	positionLabel.setTextColor(Color.WHITE);
    	positionLabel.setTypeface(tf);
    	positionLabel.setTextAppearance(this, R.style.text_style);
    	tr.addView(positionLabel);
    	
    	TextView labelName = new TextView(this);
    	labelName.setId(200 + i);
    	labelName.setText(name);
    	labelName.setPadding(2, 0, 5, 0);
    	labelName.setTextColor(Color.WHITE);
    	labelName.setTypeface(tf);
    	labelName.setTextAppearance(this, R.style.text_style);
    	tr.addView(labelName);
    	
    	TextView labelScore = new TextView(this);
    	labelScore.setId(200 + i);
    	labelScore.setText(Integer.toString(score));
    	labelScore.setPadding(2, 0, 5, 0);
    	labelScore.setTextColor(Color.WHITE);
    	labelScore.setTypeface(tf);
    	labelScore.setTextAppearance(this, R.style.text_style);
    	tr.addView(labelScore);
    	
    	tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	public void gotoMenu(View v){
		this.finish();
	}
	
	public void gotoHighscores(View v){
		Log.i(TAG, ""+currentView);
		if(currentView == 1){
			getOnlineHighscores();
			highscoresBtn.setText(local);
		}else{
			addLocalHighscores();
			highscoresBtn.setText(worldwide);
		}
	}
}

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
    private static final String fontPath = "fonts/mvboli.ttf";
    private Typeface tf;
    private TableLayout tl;
    private TextView errMsg;
	private List<ParseObject> scores;
    private int currentView = 1;
    private Button highscoresBtn;
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		Parse.initialize(this, "w7caHYa8ssCTvQrbDEvYgR4U0JeUunBylIwMZ7sU", "rEOP8nJx28SjmGFBrfYXT96LgSMH5Dq77jJ66X0W");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    setContentView(R.layout.layout_highscores);
	    tf = Typeface.createFromAsset(getAssets(), fontPath);
	    errMsg = (TextView) findViewById(R.id.errMessage);
	    errMsg.setTypeface(tf);

		highscoresBtn = (Button) findViewById(R.id.highscoresBtn);
		highscoresBtn.setText("Wereldwijd");
		
        addLocalHighscores();
	}
	
	private void addLocalHighscores(){
		errMsg.setVisibility(View.VISIBLE);
		if(currentView == 2){
			tl.removeAllViews();
			currentView = 1;
		}
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
	
	private void getOnlineHighscores(){
		errMsg.setVisibility(View.VISIBLE);
		tl.removeAllViews();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("highscores");
		query.orderByDescending("Score");
		query.setLimit(10);
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		            Log.i("score", "Retrieved " + scoreList.size() + " scores");
		            scores = scoreList;
		            if(scoreList.size() == 0){
		            	displayErrorMessage();
		            }else{
		            	errMsg.setVisibility(View.GONE);
		            	displayOnlineScores();
		            }
		        } else {
		            Log.i("score", "Error: " + e.getMessage());
		            displayErrorMessage();
		        }
		    }
		});
	}
	
	public void displayErrorMessage(){
		errMsg.setText("Er kunnen geen resultaten worden weergegeven");
		errMsg.setTypeface(tf);
	}
	
	private void displayOnlineScores(){
		addTableHeader();
		for(int i = 0; i < scores.size(); i++){
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
	    	labelName.setText(scores.get(i).getString("Name"));
	    	labelName.setPadding(2, 0, 5, 0);
	    	labelName.setTextColor(Color.WHITE);
	    	labelName.setTypeface(tf);
	    	labelName.setTextAppearance(this, R.style.text_style);
	    	tr.addView(labelName);
	    	
	    	TextView labelScore = new TextView(this);
	    	labelScore.setId(200 + i);
	    	labelScore.setText(Integer.toString(scores.get(i).getInt("Score")));
	    	labelScore.setPadding(2, 0, 5, 0);
	    	labelScore.setTextColor(Color.WHITE);
	    	labelScore.setTypeface(tf);
	    	labelScore.setTextAppearance(this, R.style.text_style);
	    	tr.addView(labelScore);
	    	
	    	tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		currentView = 2;
	}
	
	private void addTableHeader(){
		tl = (TableLayout) findViewById(R.id.main_table);
	    TableRow tr_head = new TableRow(this);
	    tr_head.setId(10);
	    tr_head.setBackgroundColor(Color.GRAY);
	    tr_head.setLayoutParams(new LayoutParams(
	    LayoutParams.WRAP_CONTENT,
	    LayoutParams.WRAP_CONTENT));
	    
	    TextView label_position = new TextView(this);
	    label_position.setId(20);
	    label_position.setText("#");
	    label_position.setTextColor(Color.WHITE);
	    label_position.setPadding(10, 5, 5, 5);
	    label_position.setTypeface(tf);
	    label_position.setTextAppearance(this, R.style.text_style);
        tr_head.addView(label_position);
        
	    TextView label_name = new TextView(this);
	    label_name.setId(20);
	    label_name.setText("Name");
	    label_name.setTextColor(Color.WHITE);
	    label_name.setPadding(2, 5, 5, 5);
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
	}
	
	public void GotoMenu(View v){
		this.finish();
	}
	
	public void GotoHighscores(View v){
		if(currentView == 1){
			getOnlineHighscores();
			highscoresBtn.setText("Lokaal");
		}else{
			addLocalHighscores();
			highscoresBtn.setText("Wereldwijd");
		}
	}
}

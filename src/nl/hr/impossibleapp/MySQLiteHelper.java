package nl.hr.impossibleapp;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Highscores";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HIGHSCORES_TABLE = "CREATE TABLE highscores ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "name TEXT, "+
                "score INTEGER )";
 
        // create table
        db.execSQL(CREATE_HIGHSCORES_TABLE);
    }
	
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS highscores");
 
        this.onCreate(db);
    }
	
private static final String TABLE_HIGHSCORES = "highscores";
	
	// Table Columns names
    //private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";
    
	//private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_SCORE};
	
	public void addHighscore(Highscores highscore){
        Log.d("addHighscore", highscore.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highscore.getName()); // get title 
        values.put(KEY_SCORE, highscore.getScore()); // get author
 
        // 3. insert
        db.insert(TABLE_HIGHSCORES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // 4. close
        db.close(); 
    }
	
	public List<Highscores> getAllHighscores() {
        List<Highscores> highscores = new LinkedList<Highscores>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_HIGHSCORES + " ORDER BY score DESC LIMIT 10";
 
    	// 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row and add it to list
        Highscores highscore = null;
        if (cursor.moveToFirst()) {
            do {
            	highscore = new Highscores();
            	highscore.setId(Integer.parseInt(cursor.getString(0)));
            	highscore.setName(cursor.getString(1));
            	highscore.setScore(Integer.parseInt(cursor.getString(2)));

            	highscores.add(highscore);
            } while (cursor.moveToNext());
        }
        
		Log.d("getAllHighscores()", highscores.toString());

        // return highscores
        return highscores;
    }
}

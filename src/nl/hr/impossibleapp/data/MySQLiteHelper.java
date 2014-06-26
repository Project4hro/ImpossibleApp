package nl.hr.impossibleapp.data;

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
        String createHighscoresTable = "CREATE TABLE highscores ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "name TEXT, "+
                "score INTEGER )";
        db.execSQL(createHighscoresTable);
    }
	
	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS highscores");
 
        this.onCreate(db);
    }
	
private static final String TABLE_HIGHSCORES = "highscores";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";
	
	public void addHighscore(Highscores highscore){
        Log.d("addHighscore", highscore.toString());

        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highscore.getName()); 
        values.put(KEY_SCORE, highscore.getScore());
 
        db.insert(TABLE_HIGHSCORES,
                null,
                values);
 
        db.close(); 
    }
	
	public List<Highscores> getAllHighscores() {
        List<Highscores> highscores = new LinkedList<Highscores>();

        String query = "SELECT  * FROM " + TABLE_HIGHSCORES + " ORDER BY score DESC LIMIT 10";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
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

        return highscores;
    }
}

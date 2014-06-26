package nl.hr.impossibleapp.gyrogame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class BallScript extends View{

    public float x;
    public float y;
    private final int r;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    
    //the contructor to create a new ball
    public BallScript(Context context, float x, float y, int r) {
        super(context);
        // set color to blue
        paint.setColor(0xff0000ff);
        // script values called from GyroscopeGame
        this.x = x;
        this.y = y;
        this.r = r;  //radius
    }      
    
    //get R
    int getR(){
    	return r;
    }
        
    //called by invalidate()
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // simply draws the circle on the screen
        canvas.drawCircle(x, y, r, paint); 
    } 
}
package nl.hr.impossibleapp.gyrogame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class SquareScript extends View {
    public float x;
    public float y;
    Paint paint = new Paint();
    private float width;
    private float height;
    private boolean up = false;
    private boolean down = true;
    private boolean left = false;
    private boolean right = false;
    
    
    public SquareScript(Context context,float x, float y, float w, float h, boolean u, boolean d,boolean l, boolean r) {
        super(context); 
        // script values called from GyroscopeGame
        this.x = x;
        this.y = y;  
        width = w;
        height = h;
        up = u;
        down = d;
        left = l;
        right = r;
    }
    @Override
    public void onDraw(Canvas canvas) { 
    	super.onDraw(canvas);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(0);
        if (up){
            canvas.drawRect((float) (width*0.46), 0, (float)(width*0.54), 60, paint);
        }else if (down){
            canvas.drawRect((float) (width*0.46), (float) (width*0.5), (float)(width*0.54), (float)(width*0.59), paint);
        }else if (left){
            canvas.drawRect(0, (float) (height*0.48), 50, (float) (height*0.58), paint);
        }else if (right){
            canvas.drawRect((float)(width*0.94), (float) (height*0.48), (float)(width*1), (float) (height*0.58), paint);
        }

    }
}     
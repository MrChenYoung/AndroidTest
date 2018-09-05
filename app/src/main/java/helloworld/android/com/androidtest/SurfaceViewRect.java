package helloworld.android.com.androidtest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SurfaceViewRect extends SurfaceViewContainer {

    private Paint paint;
    public void SurfaceViewRect(){
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    public void childrenView(Canvas canvas) {
        super.childrenView(canvas);
        canvas.drawRect(200,10,250,60,paint);
    }
}

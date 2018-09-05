package helloworld.android.com.androidtest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;

public class SurfaceViewCircle extends SurfaceViewContainer {

    private Paint paint;

    public void SurfaceViewCircle(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    @Override
    public void childrenView(Canvas canvas) {
        super.childrenView(canvas);
        canvas.drawCircle(225,35,25,paint);
    }
}

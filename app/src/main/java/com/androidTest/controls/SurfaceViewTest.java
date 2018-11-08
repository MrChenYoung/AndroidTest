package com.androidTest.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewTest extends SurfaceView implements SurfaceHolder.Callback {

    //画笔
    private Paint paint;

    private SurfaceViewContainer container;
    private SurfaceViewRect rect;
    private SurfaceViewCircle circle;


    // 构造方法
    public SurfaceViewTest(Context context){
        super(context);
        paint = new Paint();
        paint.setColor(Color.RED);

        container = new SurfaceViewContainer();
        rect = new SurfaceViewRect();
        circle = new SurfaceViewCircle();
        rect.addChildrenView(circle);
        container.addChildrenView(rect);

        getHolder().addCallback(this);
    }

    public void draw(){
        // 锁定画布
        Canvas canvas = getHolder().lockCanvas();

        // 设置画布颜色
        canvas.drawColor(Color.WHITE);

        // 画图,画一个矩形
        canvas.drawRect(10,10,100,100,paint);

        // 绘制两条线
        // 中心点反转这条线
        canvas.save();
        canvas.rotate(90,getWidth()/2,getHeight()/2);
        canvas.drawLine(0,getHeight()/2,getWidth(),getHeight(),paint);
        canvas.restore();
        canvas.drawLine(0,getHeight()/2 + 100,getWidth(),getHeight() + 100,paint);

        // 绘制组合图形
        canvas.drawRect(200,10,300,110,paint);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(250,60,50,paint);

        // 解锁画布
        getHolder().unlockCanvasAndPost(canvas);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        draw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

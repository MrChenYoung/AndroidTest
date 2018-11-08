package com.androidTest.Demos.PaintDemos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.androidTest.R;

public class RipClothesActivity extends AppCompatActivity {

    private ImageView preImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rip_clothes);

        // 获取imageview
        preImageView = findViewById(R.id.iv_pre);

        ripClothes();
    }

    // 获取原始图片，并设置手指到的地方为透明
    public void ripClothes(){
        // 获取原始图片bitmap
        Bitmap bitmapSrc = BitmapFactory.decodeResource(getResources(),R.drawable.pre19);
        // 拷贝原图
        final Bitmap bitmapCopy = Bitmap.createBitmap(bitmapSrc.getWidth(),bitmapSrc.getHeight(),bitmapSrc.getConfig());
        // 创建画笔
        Paint paint = new Paint();
        // 创建画布并初始化
        Canvas canvas = new Canvas(bitmapCopy);
        // 画画
        canvas.drawBitmap(bitmapSrc,new Matrix(),paint);
        preImageView.setImageBitmap(bitmapCopy);

        // 给图片添加触摸事件
        preImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        int ceilX = (int)Math.ceil(event.getX());
                        int ceilY = (int)Math.ceil(event.getY());
                        int floorX = (int)Math.floor(event.getX());
                        int floorY = (int)Math.floor(event.getY());

                        int space = 10;
                        for (int i = -space; i < space; i++) {
                            for (int j = -space; j < space; j++) {
                                if (Math.sqrt(i*i+j*j) < space){
                                    try {
                                        bitmapCopy.setPixel(ceilX + i,ceilY + j,Color.TRANSPARENT);
                                        bitmapCopy.setPixel(floorX,floorY,Color.TRANSPARENT);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        break;
                }

                preImageView.setImageBitmap(bitmapCopy);
                return true;
            }
        });
    }
}

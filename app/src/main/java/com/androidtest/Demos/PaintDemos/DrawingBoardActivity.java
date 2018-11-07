package com.androidtest.Demos.PaintDemos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import helloworld.android.com.androidtest.R;

public class DrawingBoardActivity extends AppCompatActivity {

    private ImageView bgImageView;
    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmapCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_board);

        // 画板背景
        bgImageView = findViewById(R.id.imageView);
        // 画画板背景
        setDrawingBoard();
        // 处理画画过程
        draw();
    }

    // 设置画板
    public void setDrawingBoard(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.drawing_board_bg);
        bitmapCopy = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());

        // 创建画笔
        paint = new Paint();
        // 创建画板
        canvas = new Canvas(bitmapCopy);
        // 画画板
        canvas.drawBitmap(bitmap,new Matrix(), paint);
        // 设置画板到界面上
        bgImageView.setImageBitmap(bitmapCopy);
    }

    // 画画
    public void draw(){
        bgImageView.setOnTouchListener(new View.OnTouchListener() {
            float startX = 0,startY = 0,stopX = 0,stopY = 0;
            boolean refreshDraw = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        // 手指按下
                        startX = event.getX();
                        startY = event.getY();
                        refreshDraw = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 手指移动
                        stopX = event.getX();
                        stopY = event.getY();
                        refreshDraw = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        // 手指抬起
                        stopX = event.getX();
                        stopY = event.getY();
                        refreshDraw = true;
                        break;
                }

                if (refreshDraw){
                    // 画线到画板上
                    canvas.drawLine(startX,startY,stopX,stopY,paint);
                    // 设置画板到界面上
                    bgImageView.setImageBitmap(bitmapCopy);

                    startX = stopX;
                    startY = stopY;
                }

                return true;
            }
        });
    }

    // 设置画笔颜色
    public void setPaintColor(View view){
        paint.setColor(Color.RED);
    }

    // 画笔加粗
    public void setPaintBold(View view){
        paint.setStrokeWidth(12);
    }

    // 保存到sd卡
    public void saveToSdcard(View view){
        // 保存到sd卡
        try {
            File saveFile = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis() + ".png");
            bitmapCopy.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(saveFile));

            // 保存成功，清空画布
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
            clearDrawBoard();

            // 通知系统刷新图库
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                // android4.4以上刷新图库
                MediaScannerConnection.scanFile(this, new String[]{saveFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        // 扫描完成
                        Toast.makeText(getApplicationContext(),"扫描图片完成",Toast.LENGTH_SHORT).show();
                    }
                });

                // 或者
//                Intent mediaScanIntent = new Intent(
//                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri contentUri = Uri.fromFile(saveFile);
//                mediaScanIntent.setData(contentUri);
//                this.sendBroadcast(mediaScanIntent);
            }else {
                // android4.4以下版本刷新图库
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
//                Uri uri = Uri.fromFile(Environment.getExternalStorageDirectory());
//                intent.setData(uri);
//                sendBroadcast(intent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 保存到内部存储卡
    public void saveToInnerDisk(View view){
        try {
            File saveFile = new File(getFilesDir(),System.currentTimeMillis() + ".png");
            bitmapCopy.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(saveFile));

            // 插入系统图库
            MediaStore.Images.Media.insertImage(getContentResolver(),bitmapCopy,"title","description");

            // 保存成功，清空画布
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
            clearDrawBoard();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 清空画布
    public void clearDrawBoard(){
        PorterDuffXfermode clearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        paint.setXfermode(clearMode);
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        setDrawingBoard();
    }
}

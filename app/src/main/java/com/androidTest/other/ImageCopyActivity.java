package com.androidTest.other;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.androidTest.R;

public class ImageCopyActivity extends AppCompatActivity {

    private ImageView imageViewSrc;
    private ImageView imageViewCopy;
    private ImageView imageViewRotate;
    private ImageView imageViewScale;
    private ImageView imageViewTranslate;
    private ImageView imageViewInverted;
    private ImageView imageViewMirror;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_copy);

        // 找到imageView
        imageViewSrc = findViewById(R.id.iv_src);
        imageViewCopy = findViewById(R.id.iv_copy);
        imageViewRotate = findViewById(R.id.iv_rotate);
        imageViewScale = findViewById(R.id.iv_scale);
        imageViewTranslate = findViewById(R.id.iv_translate);
        imageViewMirror = findViewById(R.id.iv_mirror);
        imageViewInverted = findViewById(R.id.iv_inverted);

        // 设置原图到界面上
        setImageSrc();

        // 拷贝原图的副本
        copyImage();

        // 旋转图片
        rotateImage();

        // 图片缩小0.5倍
        shrinkScale();

        // 平移图片
        translateImage();

        // 镜面效果
        mirrorImage();

        // 倒影效果
        invertedImage();
    }

    // 设置原图到界面上
    public void setImageSrc(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.scenery);
        imageViewSrc.setImageBitmap(bitmap);
    }

    // 拷贝原图的副本，是为了修改图片(比如修改图片上指定像素的颜色),因为直接修改原图会报错
    public void copyImage(){
        // 获取原图
        Bitmap bitmapScr = BitmapFactory.decodeResource(getResources(),R.drawable.scenery);

        // 创建一个模板 相当于创建一个和原图一样大小的空白白纸
        Bitmap bitmapCopy = Bitmap.createBitmap(bitmapScr.getWidth(),bitmapScr.getHeight(),bitmapScr.getConfig());
        // 创建一个画笔
        Paint paint = new Paint();
        // 创建一个画布 把白纸铺到画布上
        Canvas canvas = new Canvas(bitmapCopy);

        // 把原图画到画布上
        canvas.drawBitmap(bitmapScr,new Matrix(),paint);

        // 修改副本图片,修改指定像素的颜色
        for (int i = 0; i < 50;i++){
            for (int j = 0; j < 20; j++){
                bitmapCopy.setPixel(20+i,30+j,Color.RED);
            }
        }

        // 把图片的副本设置到界面上
        imageViewCopy.setImageBitmap(bitmapCopy);
    }

    // 图片旋转45度
    public void rotateImage(){
        // 获取原图
        Bitmap bitmapScr = BitmapFactory.decodeResource(getResources(),R.drawable.scenery);

        // 创建一个模板 相当于创建一个和原图一样大小的空白白纸
        Bitmap bitmapCopy = Bitmap.createBitmap(bitmapScr.getWidth(),bitmapScr.getHeight(),bitmapScr.getConfig());
        // 创建一个画笔
        Paint paint = new Paint();
        // 创建一个画布 把白纸铺到画布上
        Canvas canvas = new Canvas(bitmapCopy);

        Matrix matrix = new Matrix();
        // 旋转
        matrix.setRotate(45,bitmapCopy.getWidth()/2,bitmapCopy.getHeight()/2);
        // 把修改后的原图画到画布上
        canvas.drawBitmap(bitmapScr,matrix,paint);
        imageViewRotate.setImageBitmap(bitmapCopy);
    }

    // 图片缩小0.5倍
    public void shrinkScale(){
        // 获取原图
        Bitmap bitmapScr = BitmapFactory.decodeResource(getResources(),R.drawable.scenery);

        // 创建一个模板 相当于创建一个和原图一样大小的空白白纸
        Bitmap bitmapCopy = Bitmap.createBitmap(bitmapScr.getWidth(),bitmapScr.getHeight(),bitmapScr.getConfig());
        // 创建一个画笔
        Paint paint = new Paint();
        // 创建一个画布 把白纸铺到画布上
        Canvas canvas = new Canvas(bitmapCopy);

        Matrix matrix = new Matrix();
        // 缩小0.5倍
        matrix.setScale(0.5f,0.5f);
        // 把修改后的原图画到画布上
        canvas.drawBitmap(bitmapScr,matrix,paint);
        imageViewScale.setImageBitmap(bitmapCopy);
    }

    // 平移
    public void translateImage(){
        // 获取原图
        Bitmap bitmapScr = BitmapFactory.decodeResource(getResources(),R.drawable.scenery);

        // 创建一个模板 相当于创建一个和原图一样大小的空白白纸
        Bitmap bitmapCopy = Bitmap.createBitmap(bitmapScr.getWidth(),bitmapScr.getHeight(),bitmapScr.getConfig());
        // 创建一个画笔
        Paint paint = new Paint();
        // 创建一个画布 把白纸铺到画布上
        Canvas canvas = new Canvas(bitmapCopy);

        Matrix matrix = new Matrix();
        // 平移图片
        matrix.setTranslate(bitmapScr.getWidth() * 0.5f,0);
        // 把修改后的原图画到画布上
        canvas.drawBitmap(bitmapScr,matrix,paint);
        imageViewTranslate.setImageBitmap(bitmapCopy);
    }

    // 镜面效果
    public void mirrorImage(){
        // 获取原图
        Bitmap bitmapScr = BitmapFactory.decodeResource(getResources(),R.drawable.scenery);

        // 创建一个模板 相当于创建一个和原图一样大小的空白白纸
        Bitmap bitmapCopy = Bitmap.createBitmap(bitmapScr.getWidth(),bitmapScr.getHeight(),bitmapScr.getConfig());
        // 创建一个画笔
        Paint paint = new Paint();
        // 创建一个画布 把白纸铺到画布上
        Canvas canvas = new Canvas(bitmapCopy);

        Matrix matrix = new Matrix();
        // 设置y为负
        matrix.setScale(-1.0f,1.0f);
        // 移动宽度的距离
        matrix.postTranslate(bitmapScr.getWidth(),0);
        // 把修改后的原图画到画布上
        canvas.drawBitmap(bitmapScr,matrix,paint);
        imageViewMirror.setImageBitmap(bitmapCopy);
    }

    // 倒影效果
    public void invertedImage(){
        // 获取原图
        Bitmap bitmapScr = BitmapFactory.decodeResource(getResources(),R.drawable.scenery);

        // 创建一个模板 相当于创建一个和原图一样大小的空白白纸
        Bitmap bitmapCopy = Bitmap.createBitmap(bitmapScr.getWidth(),bitmapScr.getHeight(),bitmapScr.getConfig());
        // 创建一个画笔
        Paint paint = new Paint();
        // 创建一个画布 把白纸铺到画布上
        Canvas canvas = new Canvas(bitmapCopy);

        Matrix matrix = new Matrix();
        // 设置x为负
        matrix.setScale(1.0f,-1.0f);
        // 移动高度的距离
        matrix.postTranslate(0,bitmapScr.getHeight());
        // 把修改后的原图画到画布上
        canvas.drawBitmap(bitmapScr,matrix,paint);
        imageViewInverted.setImageBitmap(bitmapCopy);
    }
}

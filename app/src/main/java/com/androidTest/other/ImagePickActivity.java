package com.androidTest.other;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import com.androidTest.R;

public class ImagePickActivity extends AppCompatActivity {

    // 打开系统相机拍照完成
    private final int TAKEPICSUCCWITHPATH = 1;
    private final int TAKEPICSUCCWITHOUTPATH = 2;

    // 显示照片
    private ImageView imageView;
    private ImageView imageView2;
    private TextView textView1;
    private TextView textView2;


    // 拍照存储路径
    private final String takePicturePath = Environment.getExternalStorageDirectory() +
            File.separator + Environment.DIRECTORY_DCIM + File.separator;
    private File picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick);

        // 设置调用系统播放器
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // 展示拍照完成的照片
        imageView = findViewById(R.id.iv_takePicture1);
        imageView2 = findViewById(R.id.iv_takePicture2);
        textView1 = findViewById(R.id.tv_text1);
        textView2 = findViewById(R.id.tv_text2);
    }

    // 打开相机拍照(设置照片存储路径,获取到的图片为原图，尺寸大)
    public void tackPicture1(View view){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);

        // 设置照片名和路径
        picturePath = new File(takePicturePath,System.currentTimeMillis() + ".jpg");
        Uri uri = Uri.fromFile(picturePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,TAKEPICSUCCWITHPATH);
    }

    // 打开系统相机(不设置存储路径,默认路径存储,获取到的图片为压缩后的照片,尺寸小)
    public void tackPicture2(View view){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent,TAKEPICSUCCWITHOUTPATH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == TAKEPICSUCCWITHPATH){
            // 拍照完成
            Uri uri = Uri.fromFile(picturePath);
            imageView.setImageURI(uri);

            // 显示图片尺寸和大小
            StringBuilder stringBuilder = new StringBuilder();
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath.getAbsolutePath());
            stringBuilder.append("尺寸:" + bitmap.getWidth() + "X" + bitmap.getHeight() + "\n");
            stringBuilder.append("大小:" + Formatter.formatFileSize(this,picturePath.length()));
            textView1.setText(stringBuilder.toString());
        }else if (requestCode == TAKEPICSUCCWITHOUTPATH){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView2.setImageBitmap(bitmap);

            // 显示图片尺寸和大小
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("尺寸:" + bitmap.getWidth() + "X" + bitmap.getHeight() + "\n");

            try{
                File file = new File(takePicturePath,System.currentTimeMillis() + ".jpg");
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                stringBuilder.append("大小:" + Formatter.formatFileSize(this,file.length()));
                textView2.setText(stringBuilder.toString());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}

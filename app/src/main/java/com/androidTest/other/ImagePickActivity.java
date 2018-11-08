package com.androidTest.other;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import com.androidTest.R;

public class ImagePickActivity extends AppCompatActivity {

    // 打开系统相机拍照完成
    private final int TAKEPICTURESUCCESS = 1;

    private ImageView imageView;
    private File takePicturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pick);

        // 设置调用系统播放器
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // 展示拍照完成的照片
        imageView = findViewById(R.id.iv_takePicture);

        File file = new File(getFilesDir(),"ceshi.jpg");
        if (file.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }

    // 打开相机拍照
    public void tackPicture(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicturePath = new File(getFilesDir(),  "ceshi.jpg");
        Uri uri = Uri.fromFile(takePicturePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,TAKEPICTURESUCCESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKEPICTURESUCCESS){
            // 拍照完成
            Bitmap bitmap = BitmapFactory.decodeFile(takePicturePath.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }
}

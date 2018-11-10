package com.androidTest.other;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
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
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.List;

import com.androidTest.R;
import com.androidTest.main.PermissionBaseActivity;
import com.utiles.Permissons;
import com.utiles.UriUtil;

import org.w3c.dom.Text;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ImagePickActivity extends PermissionBaseActivity {

    // 打开系统相机拍照完成
    private final int TAKEPICSUCCWITHPATH = 1;
    private final int TAKEPICSUCCWITHOUTPATH = 2;
    private final int CROPPICTURECODE = 3;
    private final int TAKEVIDEOCODE = 4;
    private final int REQUESTALBUMCODE = 5;

    // 申请权限
    private final int PERMISSION_CAMERA1 = 100;
    private final int PERMISSION_CAMERA2 = 200;
    private final int PERMISSION_CAMERA3 = 300;
    private final int PERMISSION_WRITE_EXTERNAL_STORAGE = 400;

    // 相机权限
    private final String[] cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    // 显示照片
    private ImageView imageView;
    private ImageView imageView2;
    private TextView textView1;
    private TextView textView2;


    // 拍照存储路径
    private final String takePicturePath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator;
    private File picturePath;
    private File videoPath;
    private TextView textView3;
    private VideoView videoView;
    private ImageView imageView3;
    private TextView textView4;

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
        imageView3 = findViewById(R.id.iv_takePicture3);
        textView1 = findViewById(R.id.tv_text1);
        textView2 = findViewById(R.id.tv_text2);
        textView3 = findViewById(R.id.tv_text3);
        textView4 = findViewById(R.id.tv_text4);
    }

    // 打开相机拍照(设置照片存储路径,获取到的图片为原图，尺寸大)
    public void tackPicture1(View view) {
        if (Permissons.hasPermissions(this,this,PERMISSION_CAMERA1,"请求使用相机",cameraPermission)){
            takePicture1();
        }else {
            // 没有权限

        }
    }

    // 打开相机拍照
    // AfterPermissionGranted标识的方法,在授权以后会直接调用,要求方法不能有参数
    @AfterPermissionGranted(PERMISSION_CAMERA1)
    private void takePicture1(){
        if (haveCamera()){
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            // 设置照片名和路径
            picturePath = new File(takePicturePath, System.currentTimeMillis() + ".jpg");
            Uri uri = Uri.fromFile(picturePath);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, TAKEPICSUCCWITHPATH);
        }
    }

    // 打开系统相机(不设置存储路径,默认路径存储,获取到的图片为压缩后的照片,尺寸小)
    public void tackPicture2(View view) {
        if (Permissons.hasPermissions(this,this,PERMISSION_CAMERA2,"请求使用相机",cameraPermission)){
            takePicture2();
        }else {
            // 没有权限
        }
    }

    @AfterPermissionGranted(PERMISSION_CAMERA2)
    public void takePicture2(){
        if (haveCamera()){
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, TAKEPICSUCCWITHOUTPATH);
        }
    }

    // 打开相机拍视频
    //@AfterPermissionGranted：权限授权回调，当用户在授权之后，会回调带有AfterPermissionGranted对应权限的方法
    public void takeVideo(View view) {
        if (Permissons.hasPermissions(this,this,PERMISSION_CAMERA3,"请求使用相机",cameraPermission)){
            takeVideo();
        }else {
            // 没有权限 去申请

        }
    }

    @AfterPermissionGranted(PERMISSION_CAMERA3)
    public void takeVideo(){
        // 有权限
        if (haveCamera()){
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            // 设置存储路径(经测试指定路径下不能存储视频,只能从onActivityResult方法中获取)
            videoPath = new File("/sdcard/" + System.currentTimeMillis() + ".mp4");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoPath.getAbsolutePath());
            startActivityForResult(intent, TAKEVIDEOCODE);
        }
    }


    // 打开系统相册
    public void openAlbum(View view) {
        if (Permissons.hasPermissions(this,this,PERMISSION_WRITE_EXTERNAL_STORAGE,"请求存储权限",new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})){
            openAlbum();
        }else {
            // 没有权限

        }
    }

    // 打开相册
    @AfterPermissionGranted(PERMISSION_WRITE_EXTERNAL_STORAGE)
    private void openAlbum(){
        Intent intent = new Intent();
        // 打开相册
        intent.setAction(Intent.ACTION_PICK);
        // 显示所有照片
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        // 只能选择视频
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"video/*");
        // 只能选择照片
         intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent, REQUESTALBUMCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == TAKEPICSUCCWITHPATH) {
            // 拍照完成,裁剪
            cropPicture();
        } else if (requestCode == TAKEPICSUCCWITHOUTPATH) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView2.setImageBitmap(bitmap);

            // 显示图片尺寸和大小
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("尺寸:" + bitmap.getWidth() + "X" + bitmap.getHeight() + "\n");

            try {
                File file = new File(takePicturePath, System.currentTimeMillis() + ".jpg");
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                stringBuilder.append("大小:" + Formatter.formatFileSize(this, file.length()));
                textView2.setText(stringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == CROPPICTURECODE) {
            // 照片裁剪完成
            String name = picturePath.getName();
            File file = new File(takePicturePath + "CROP_" + name);

            Uri uri = Uri.fromFile(file);
            imageView.setImageURI(uri);

            // 显示图片尺寸和大小
            StringBuilder stringBuilder = new StringBuilder();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            stringBuilder.append("尺寸:" + bitmap.getWidth() + "X" + bitmap.getHeight() + "\n");
            stringBuilder.append("大小:" + Formatter.formatFileSize(this, file.length()));
            textView1.setText(stringBuilder.toString());
        } else if (requestCode == TAKEVIDEOCODE) {
            // 拍视频完成,获取视频
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // 视频的默认存储路径
            String path = cursor.getString(1);
            // 视频的名字
            String name = cursor.getString(2);
            cursor.close();
            File file = new File(path);

            // 显示视频大小
            String sizeFormater = Formatter.formatFileSize(this, file.length());
            textView3.setText("大小:" + sizeFormater);

            // 调用系统播放器播放视频
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(path), "video/*");
            startActivity(intent);
        } else if (requestCode == REQUESTALBUMCODE) {
            // 从相册获取到照片
            Uri uri = data.getData();
            String path = UriUtil.getPath(uri);

            File file = new File(path);
            String fileSize = Formatter.formatFileSize(this,file.length());
            textView4.setText("大小:" + fileSize);

            imageView3.setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }

    // 裁剪照片
    public void cropPicture() {
        String imageName = picturePath.getName();
        File mCropImageFile = new File(takePicturePath, "CROP_" + imageName);

        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(Uri.fromFile(picturePath), "image/*");
        intent.putExtra("crop", "true");       // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);         //X方向上的比列
        intent.putExtra("aspectY", 2);         // Y方向上的比例
        intent.putExtra("outputX", 300);       //裁剪区的宽度
        intent.putExtra("outputY", 600);       //裁剪区的高度
        intent.putExtra("scale", "true");

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);  //是否在Intent中返回数据

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        startActivityForResult(intent, CROPPICTURECODE);
    }

    // 判断手机是否有相机功能
    public boolean haveCamera(){
        if (isHaveIntent(MediaStore.ACTION_IMAGE_CAPTURE) && isHaveIntent(MediaStore.ACTION_VIDEO_CAPTURE)){
            return true;
        }else {
            Toast.makeText(this,"您的手机没有相机功能",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 判断某个意图是否存在
     */
    public boolean isHaveIntent(String intentName) {
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent(intentName);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}

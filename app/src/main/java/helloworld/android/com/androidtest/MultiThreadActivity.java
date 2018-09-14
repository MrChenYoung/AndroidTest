package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MultiThreadActivity extends Activity {
    private ImageView imageView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multithread);

        imageView = findViewById(R.id.imageView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("图片下载中,请稍后...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    // 异步下载图片
    public void downloadImage(View view) {

        File path = getImagePath();
        if (path.exists()) {
            path.delete();
        }

        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1536926459305&di=d810876eca4c97259dd095660ccc9d61&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F-4o3dSag_xI4khGko9WTAnF6hhy%2Flvpics%2Fh%3D800%2Fsign%3Db49dc48f8718367ab28972dd1e728b68%2F9922720e0cf3d7ca7f0736d0f31fbe096a63a9a6.jpg";
        new MyAsyncTask().execute(url);
    }

    // 异步类
    class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 线程执行前
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                byte[] bytes = new byte[8 * 1024];
                int length;
                int downloadLength = 0;

                FileOutputStream fos = new FileOutputStream(getImagePath());

                int totalLength = connection.getContentLength();
                while ((length = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, length);
                    downloadLength += length;

                    publishProgress((int)(downloadLength/totalLength * 100));
                }

                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return BitmapFactory.decodeFile(getImagePath().getAbsolutePath());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // 线程执行后,更新UI
            progressDialog.dismiss();
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }
    }


    // 获取下载图片的保存路径
    public File getImagePath() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "sd卡不可用", Toast.LENGTH_SHORT).show();
            return null;
        }

        File path = new File(Environment.getExternalStorageDirectory(), "downloadImage.jpg");
        if (!path.exists()) {
            try {
                boolean success = path.createNewFile();
                if (!success) {
                    Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show();
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return path;
    }

}


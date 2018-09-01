package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

public class ProgressBarActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);

    }

    // 进度加10
    public void add(View view){
        ProgressBar progressBar = findViewById(R.id.progressbar);
        // 加10
        progressBar.incrementProgressBy(10);
    }

    // 进度减10
    public void minus(View view){
        ProgressBar progressBar = findViewById(R.id.progressbar);

        // 减10
        progressBar.incrementProgressBy(-10);
    }

    // 显示环形进度条
    public void showProgressDialog1(View view){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("标题");
        progressDialog.setMessage("正在下载...");
        progressDialog.show();
    }

    // 显示条形进度条
    public void showProgressDialog2(View view){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("标题");
        progressDialog.setMessage("正在下载...");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.incrementProgressBy(30);

        progressDialog.show();
    }
}

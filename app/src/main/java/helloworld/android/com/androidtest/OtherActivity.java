package helloworld.android.com.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class OtherActivity extends Activity {


    private TextView innerStorageText;
    private TextView sdStorageText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        // 存储空间
        innerStorageText = findViewById(R.id.in_textView);
        sdStorageText = findViewById(R.id.sd_textView);
        getStorage();
    }

    // 获取手机存储空间
    public void getStorage(){
        // 获取内部存储空间
        File innerFile = Environment.getDataDirectory();
        long innerTotalSize = innerFile.getTotalSpace();
        long innerFreeSize = innerFile.getFreeSpace();
        long innerUsedSize = innerFile.getUsableSpace();

        // 格式化存储空间
        String inTotal = Formatter.formatFileSize(this,innerTotalSize);
        String inFree = Formatter.formatFileSize(this,innerFreeSize);
        String inUsed = Formatter.formatFileSize(this,innerUsedSize);

        // 获取sd卡存储
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            // sd卡被挂载,可用
            File sdFile = Environment.getExternalStorageDirectory();
            long sdTotalSize = sdFile.getTotalSpace();
            long sdFreeSize = sdFile.getFreeSpace();
            long sdUsedSize = sdFile.getUsableSpace();

            // 格式化存储空间
            String sdTotal = Formatter.formatFileSize(this,sdTotalSize);
            String sdFree = Formatter.formatFileSize(this,sdFreeSize);
            String sdUsed = Formatter.formatFileSize(this,sdUsedSize);

            innerStorageText.setText("内部存储: 共" + inTotal + ",已用" + inUsed + ",剩余" + inFree);
            sdStorageText.setText("sd卡存储: 共" + sdTotal + ",已用" + sdUsed + ",剩余"  + sdFree);
        }else {
            // sd卡不可用
            sdStorageText.setText("sd卡不可用");
        }

    }
}

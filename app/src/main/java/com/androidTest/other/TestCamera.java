package com.androidTest.other;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestCamera extends Activity {

    private String videofileName;
    private String strsavedir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            //将存储在默认目录下的文件拷贝到指定文件夹下，删除默认文件
            String str = null;
            try{
                if(requestCode == 1) {
                    Uri uri = Uri.parse(data.getData().toString());
                    ContentResolver cr = this.getContentResolver();
                    Cursor cursor = cr.query(uri, null,
                            null,
                            null,
                            null);

                    cursor.moveToFirst();
                    str = cursor.getString(1);
                    videofileName = cursor.getString(2);
                    cursor.close();
                }

                File destfile = new File(strsavedir + videofileName);
                File srcfile = new File(str);
                moveFileTo(srcfile,destfile);
                super.onActivityResult(requestCode,
                        resultCode, data);
            } catch(Exception e) {

            }
        }
    }

    public boolean moveFileTo(File srcFile, File destFile) throws IOException {
        boolean iscopy = copyFileTo(srcFile, destFile);
        if(!iscopy) return false;
        delFile(srcFile);
        return true;
    }

    public boolean copyFileTo(File srcFile, File destFile) throws IOException {
        if(srcFile.isDirectory() || destFile.isDirectory()) return false;

        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while((readLen = fis.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }

        fos.flush();
        fos.close();
        fis.close();
        return true;
    }

    public boolean delFile(File file) {
        if (file.isDirectory())
            return false;
        return file.delete();
    }
}

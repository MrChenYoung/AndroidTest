package com.androidtest.elements;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ContentObserverHandle extends Handler {

    // 上下文
    private Context context;

    public ContentObserverHandle(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        String message = null;
        switch (msg.what){
            case 1:
                // 查询
                message = "查询了数据库";
                break;
            case 2:
                // 插入
                message = "插入了数据";
                break;
            case 3:
                // 删除
                message = "删除了数据";
                break;
            case 4:
                // 修改
                message = "修改了数据";
                break;
            case 5:
                //
                message = "对数据库操作了";
                break;
        }

        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}

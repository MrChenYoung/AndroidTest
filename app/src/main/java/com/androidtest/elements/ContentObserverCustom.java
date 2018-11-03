package com.androidtest.elements;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ContentObserverCustom extends ContentObserver {

    // 上下文
    private Context context;
    // handle
    private Handler handler;

    public ContentObserverCustom(Context context, Handler handler) {
        super(handler);

        this.context = context;
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

        if (uri.toString().equals("content://com.AndroidAssistant.provider/qury")){
            // 查询远程数据库
            Message message = Message.obtain();
            message.what = 1;
            handler.sendMessage(message);
        }else if (uri.toString().equals("content://com.AndroidAssistant.provider/insert")) {
            // 向远程数据库插入数据
            Message message = Message.obtain();
            message.what = 2;
            handler.sendMessage(message);
        }else if (uri.toString().equals("content://com.AndroidAssistant.provider/delete")){
            // 删除远程数据库里面的数据
            Message message = Message.obtain();
            message.what = 3;
            handler.sendMessage(message);
        }else if (uri.toString().equals("content://com.AndroidAssistant.provider/update")){
            // 修改远程数据库数据
            Message message = Message.obtain();
            message.what = 4;
            handler.sendMessage(message);
        }
    }
}

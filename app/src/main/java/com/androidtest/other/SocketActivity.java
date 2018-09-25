package com.androidtest.other;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import helloworld.android.com.androidtest.R;

public class SocketActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

    }

    // 开启socket服务器
    public void startServer(View view){
        try {
            // 创建服务器socket
            final ServerSocket serverSocket = new ServerSocket(12345);
            serverSocket.accept();

            // 监听客户端的链接
//            new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        serverSocket.accept();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    // 循环监听客户端链接
//                    while (true){
//                        try {
//                            Socket socket = serverSocket.accept();
//
//                            int count = 0;
//                            while (true){
//                                // 新建线程发送数据给客户端
//                                socket.getOutputStream().write((count + "").getBytes());
//                                count++;
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 新线程
    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();

        }
    }
}

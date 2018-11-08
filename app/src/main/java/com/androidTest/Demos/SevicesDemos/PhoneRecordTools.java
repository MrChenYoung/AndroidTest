package com.androidTest.Demos.SevicesDemos;

import android.content.Context;
import android.media.MediaRecorder;

import java.io.IOException;

public class PhoneRecordTools {

    // 录音器
    private MediaRecorder mediaRecorder;

    private Context context;

    public PhoneRecordTools(Context context){
        this.context = context;

        // 初始化录音器
        initialRecorder();
    }

    // 初始化录音器
    public void initialRecorder(){
        if (mediaRecorder == null){

            mediaRecorder = new MediaRecorder();

            //从麦克风采集声音数据
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置保存文件格式为MP4
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //设置采样频率,44100是所有安卓设备都支持的频率,频率越高，音质越好，当然文件越大
            mediaRecorder.setAudioSamplingRate(44100);
            //设置声音数据编码格式,音频通用格式是AAC
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //设置编码频率
            mediaRecorder.setAudioEncodingBitRate(96000);

//            // 录音器
//            mediaRecorder = new MediaRecorder();
//
//            // 设置源 MediaRecorder.AudioSource.MIC麦克风(只能录单向的声音)  MediaRecorder.AudioSource.VOICE_CALL可以双向录音
//            try {
//                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
//            } catch (RuntimeException e) {
//                e.printStackTrace();
//            }
//
//            //设置声音数据编码格式,音频通用格式是AAC
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//
//            // 设置输出格式 THREE_GPP 3GP格式 MPEG_4 MP4格式
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//
//            //设置采样频率,44100是所有安卓设备都支持的频率,频率越高，音质越好，当然文件越大
//            mediaRecorder.setAudioSamplingRate(44100);
//
//            //设置编码频率
//            mediaRecorder.setAudioEncodingBitRate(96000);
        }
    }


    // 准备录音
    public void prepareRecord(){
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 开始录音
    public void startRecord(){
        if (mediaRecorder != null){
            mediaRecorder.start();
        }
    }

    // 停止录音
    public void stopRecord(){
        if (mediaRecorder != null){
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                // 如果当前java状态和jni里面的状态不一致，
                mediaRecorder.release();
            }
        }
    }

    // 设置输出路径
    public void setOutputFile(String outputFile){
        if (mediaRecorder != null){
            mediaRecorder.setOutputFile(outputFile);
        }
    }
}

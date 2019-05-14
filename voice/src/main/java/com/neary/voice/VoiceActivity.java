package com.neary.voice;


import java.io.File;
import java.io.IOException;



import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class VoiceActivity extends Activity implements OnClickListener {
    private static final Object FileUtil = ;
    private Button Startrecording,stoptrecording,Playtape;
    private MediaRecorder  recorder;
    private MediaPlayer player;
    private String voicePath ;
    private long time;
    //private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        //new一个实例
        player = new MediaPlayer();
        Startrecording = (Button) findViewById(R.id.Startrecording);
        stoptrecording = (Button) findViewById(R.id.stoptrecording);
        Playtape = (Button) findViewById(R.id.Playtape);
        //点击事件
        Playtape.setOnClickListener(this);
        Startrecording.setOnClickListener(this);
        stoptrecording.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Startrecording://开始录音
                fasong();
                break;
            case R.id.stoptrecording://结束录音
                jieshu();
                break;
            case R.id.Playtape://播放录音
                play();
                break;
            default:
                break;
        }
    }

    /**
     * 发送语音
     */
    private void fasong(){
        if(recorder != null){
            //不等于空的时候让他变闲置
            recorder.reset();
        }else{
            recorder = new MediaRecorder();
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //输出格式
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        //设置音频编码器
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //缓存目录
        String str = FileUtil.getSDCardPath()+"/A微信/voice";
        //检查该目录是否存在  否则创建
        FileUtil.checkDir(str);
        //设置文件名
        voicePath = str+ System.currentTimeMillis()+".amr";
        //设置录音的输出路径
        recorder.setOutputFile(voicePath);

        try {
            recorder.prepare();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        recorder.start();
        time = System.currentTimeMillis();
    }

    /**
     * 结束语音
     */
    private void jieshu(){
        recorder.stop();
        long shijian =System.currentTimeMillis() - time;
        if(shijian<1000){//判断，如果录音时间小于一秒，则删除文件提示，过短
            File file = new File(voicePath);
            if(file.exists()){//判断文件是否存在，如果存在删除文件
                file.delete();//删除文件
                Toast.makeText(VoiceActivity.this, "录音时间过短",Toast.LENGTH_SHORT).show();
            }
        }
        //重置
        recorder.release();
        if(recorder != null){
            recorder.release();
            recorder = null;
            System.gc();
        }
    }

    /**
     * 播放录音
     */
    private void play(){
        if(player != null){
            player.reset();
            try {
                //设置语言的来源
                player.setDataSource(voicePath);
                //初始化
                player.prepare();
                //开始播放
                player.start();
            }catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
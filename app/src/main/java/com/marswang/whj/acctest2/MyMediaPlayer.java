package com.marswang.whj.acctest2;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by whj on 2016/3/11 0011.
 */
public class MyMediaPlayer {
    private MediaPlayer mPlayer;

    public void stop(){
        if(mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }
    }
    public void paly(Context context,int rawRes){
        stop();
        mPlayer = MediaPlayer.create(context,rawRes);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
        mPlayer.start();
    }
}
